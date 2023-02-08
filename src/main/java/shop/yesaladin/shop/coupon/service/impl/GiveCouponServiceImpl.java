package shop.yesaladin.shop.coupon.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage.CouponCodesAndResultMessageBuilder;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원에게 쿠폰을 지급하는 서비스 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class GiveCouponServiceImpl implements GiveCouponService {

    private static final String COUPON_GROUP_CODE_REQUEST_URL_PREFIX = "coupon-groups";

    private final GatewayProperties gatewayProperties;
    private final CouponProducer couponProducer;
    private final QueryMemberCouponRepository queryMemberCouponRepository;
    private final CommandMemberCouponRepository commandMemberCouponRepository;
    private final QueryMemberService queryMemberService;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public void sendCouponGiveRequest(
            String memberId, TriggerTypeCode triggerTypeCode, Long couponId
    ) {
        List<CouponGroupAndLimitDto> couponGroupAndLimitList = getCouponGroupAndLimit(
                triggerTypeCode,
                couponId
        );

        List<String> couponGroupCodeList = couponGroupAndLimitList.stream()
                .map(CouponGroupAndLimitDto::getCouponGroupCode)
                .collect(Collectors.toList());

        checkMemberAlreadyHasCoupon(memberId, triggerTypeCode, couponId, couponGroupCodeList);

        couponGroupAndLimitList.forEach(couponGroupAndLimit -> {
            String requestId = generateRequestId(memberId);

            sendGiveRequestMessage(
                    triggerTypeCode,
                    couponId,
                    couponGroupAndLimit.getIsLimited(),
                    requestId
            );
        });

    }

    @Override
    @Transactional
    public void giveCouponToMember(CouponGiveRequestResponseMessage responseMessage) {
        CouponCodesAndResultMessageBuilder resultBuilder = CouponCodesAndResultMessage.builder()
                .couponCodes(responseMessage.getCoupons()
                        .stream()
                        .flatMap(coupon -> coupon.getCouponCodes().stream())
                        .collect(Collectors.toList()));
        try {
            checkRequestSucceeded(responseMessage);
            String memberId = getMemberIdFromRequestId(responseMessage.getRequestId());
            tryGiveCouponToMember(responseMessage, memberId);
            couponProducer.produceGivenResultMessage(resultBuilder.success(true).build());
        } catch (Exception e) {
            couponProducer.produceGivenResultMessage(resultBuilder.success(false).build());
            throw e;
        }

    }

    private List<CouponGroupAndLimitDto> getCouponGroupAndLimit(
            TriggerTypeCode triggerTypeCode, Long couponId
    ) {
        String requestUri = UriComponentsBuilder.fromUriString(gatewayProperties.getCouponUrl())
                .pathSegment("v1")
                .pathSegment(COUPON_GROUP_CODE_REQUEST_URL_PREFIX)
                .queryParam("trigger-type", triggerTypeCode.name())
                .queryParamIfPresent("coupon-id", Optional.ofNullable(couponId))
                .toUriString();
        return requestToGetCouponGroupAndLimitList(triggerTypeCode, couponId, requestUri);
    }

    private List<CouponGroupAndLimitDto> requestToGetCouponGroupAndLimitList(
            TriggerTypeCode triggerTypeCode, Long couponId, String requestUri
    ) {
        try {
            ResponseEntity<ResponseDto<List<CouponGroupAndLimitDto>>> response = restTemplate.exchange(
                    requestUri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new ServerException(
                            ErrorCode.INTERNAL_SERVER_ERROR,
                            "Receive bad response from coupon server."
                    ))
                    .getData();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new ClientException(
                        ErrorCode.NOT_FOUND,
                        "Coupon group not exists. Trigger : " + triggerTypeCode + " coupon id : "
                                + couponId
                );
            }
            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Cannot send request. status : " + e.getStatusCode()
            );
        }
    }

    private void checkMemberAlreadyHasCoupon(
            String memberId,
            TriggerTypeCode triggerTypeCode,
            Long couponId,
            List<String> couponGroupCodeList
    ) {
        boolean memberAlreadyHasCoupon = queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                memberId,
                couponGroupCodeList
        );

        if (memberAlreadyHasCoupon) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Member already has requested coupon. Member id" + memberId
                            + ", trigger type : " + triggerTypeCode + ", coupon id : " + couponId
            );
        }
    }

    private String generateRequestId(String memberId) {
        String requestId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(requestId, memberId, Duration.ofMinutes(30));
        return requestId;
    }

    private void sendGiveRequestMessage(
            TriggerTypeCode triggerTypeCode, Long couponId, boolean isLimited, String requestId
    ) {
        CouponGiveRequestMessage giveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(triggerTypeCode)
                .couponId(couponId)
                .build();

        if (isLimited) {
            couponProducer.produceGiveRequestLimitMessage(giveRequestMessage);
            return;
        }
        couponProducer.produceGiveRequestMessage(giveRequestMessage);
    }

    private void checkRequestSucceeded(CouponGiveRequestResponseMessage responseMessage) {
        if (!responseMessage.isSuccess()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, responseMessage.getErrorMessage());
        }
    }

    private String getMemberIdFromRequestId(String requestId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(requestId))
                .orElseThrow(() -> new ClientException(
                        ErrorCode.BAD_REQUEST,
                        "Request id not exists or expired. request id : " + requestId
                ));
    }

    private void tryGiveCouponToMember(
            CouponGiveRequestResponseMessage responseMessage, String memberId
    ) {
        Member member = queryMemberService.findMemberByLoginId(memberId).toEntity();
        responseMessage.getCoupons().forEach(coupon ->
                coupon.getCouponCodes()
                        .stream()
                        .map(code -> MemberCoupon.builder()
                                .member(member)
                                .couponCode(code)
                                .couponGroupCode(coupon.getCouponGroupCode())
                                .expirationDate(coupon.getExpirationDate())
                                .build())
                        .forEach(commandMemberCouponRepository::save)

        );
    }
}
