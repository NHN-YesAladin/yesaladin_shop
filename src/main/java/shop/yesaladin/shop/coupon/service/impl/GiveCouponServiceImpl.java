package shop.yesaladin.shop.coupon.service.impl;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import shop.yesaladin.coupon.code.CouponSocketRequestKind;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage.CouponCodesAndResultMessageBuilder;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.coupon.message.CouponResultDto;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.event.CouponRequestProcessEndEvent;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * ???????????? ????????? ???????????? ????????? ?????????????????? ??????????????????.
 *
 * @author ?????????
 * @author ?????????
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GiveCouponServiceImpl implements GiveCouponService {

    private static final String COUPON_GROUP_CODE_REQUEST_URL_PREFIX = "coupon-groups";
    private static final String MONTHLY_POLICY_KEY = "monthlyCouponPolicy";
    private static final String MONTHLY_COUPON_ID_KEY = "monthlyCouponId";
    private static final String MONTHLY_COUPON_OPEN_DATE_TIME_KEY = "monthlyCouponOpenDateTime";

    private final GatewayProperties gatewayProperties;
    private final CouponProducer couponProducer;
    private final QueryMemberCouponRepository queryMemberCouponRepository;
    private final CommandMemberCouponRepository commandMemberCouponRepository;
    private final QueryMemberService queryMemberService;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public RequestIdOnlyDto sendCouponGiveRequest(
            String memberId,
            TriggerTypeCode triggerTypeCode,
            Long couponId,
            LocalDateTime requestDateTime
    ) {
        if (isMonthlyCoupon(triggerTypeCode)) {
            checkMonthlyCouponIssueRequestTime(requestDateTime);
        }

        if (Objects.nonNull(couponId)) {    // ?????? ?????? ?????? ????????? ???????????? ??????
            registerIssueRequest(memberId, triggerTypeCode.name(), couponId.toString());
        }
        List<CouponGroupAndLimitDto> couponGroupAndLimitList = getCouponGroupAndLimit(
                triggerTypeCode,
                couponId
        );

        List<String> couponGroupCodeList = couponGroupAndLimitList.stream()
                .map(CouponGroupAndLimitDto::getCouponGroupCode)
                .collect(Collectors.toList());

        checkMemberAlreadyHasCoupon(memberId, triggerTypeCode, couponId, couponGroupCodeList);

        String requestId = generateRequestId(memberId);
        RequestIdOnlyDto response = new RequestIdOnlyDto(requestId);

        if (Objects.isNull(couponId)) {
            sendGiveRequestMessage(
                    triggerTypeCode,
                    null,
                    couponGroupAndLimitList.get(0).getIsLimited(),
                    requestId
            );
            return response;
        }

        couponGroupAndLimitList.forEach(couponGroupAndLimit -> sendGiveRequestMessage(
                triggerTypeCode,
                couponId,
                couponGroupAndLimit.getIsLimited(),
                requestId
        ));
        return response;
    }

    @Override
    @Transactional
    public void giveCouponToMember(CouponGiveRequestResponseMessage responseMessage) {
        CouponCodesAndResultMessageBuilder resultBuilder = CouponCodesAndResultMessage.builder();
        try {
            checkRequestSucceeded(responseMessage);
            String memberId = getMemberIdFromRequestId(responseMessage.getRequestId());
            checkMemberAlreadyHasCoupon(
                    memberId,
                    null,
                    null,
                    responseMessage.getCoupons()
                            .stream()
                            .map(CouponGiveDto::getCouponGroupCode)
                            .collect(Collectors.toList())
            );
            resultBuilder.couponCodes(responseMessage.getCoupons()
                    .stream()
                    .flatMap(coupon -> coupon.getCouponCodes().stream())
                    .collect(Collectors.toList()));
            tryGiveCouponToMember(responseMessage, memberId);
            couponProducer.produceGivenResultMessage(resultBuilder.success(true).build());

            CouponResultDto resultMessage = new CouponResultDto(
                    CouponSocketRequestKind.GIVE,
                    responseMessage.getRequestId(),
                    responseMessage.isSuccess(),
                    responseMessage.isSuccess() ? "????????? ?????????????????????."
                            : responseMessage.getErrorMessage(),
                    LocalDateTime.now(clock)
            );
            eventPublisher.publishEvent(new CouponRequestProcessEndEvent(this, resultMessage));
        } catch (Exception e) {
            couponProducer.produceGivenResultMessage(resultBuilder.success(false).build());
            CouponResultDto resultMessage = new CouponResultDto(
                    CouponSocketRequestKind.GIVE,
                    responseMessage.getRequestId(),
                    responseMessage.isSuccess(),
                    responseMessage.getErrorMessage(),
                    LocalDateTime.now(clock)
            );
            eventPublisher.publishEvent(new CouponRequestProcessEndEvent(this, resultMessage));
            throw e;
        }
    }

    /**
     * ?????? ?????? ????????? ?????? ????????? 10??? ?????? redis ??? ???????????????. 10??? ?????? ?????? ????????? ???????????? ?????? ????????? ????????????.
     *
     * @param memberId        ?????? ????????? ??? ????????? ????????? ?????????
     * @param triggerTypeCode ?????? ????????? ??? ????????? ????????? ?????? ??????
     * @param couponId        ?????? ????????? ????????? ?????????
     */
    public void registerIssueRequest(
            String memberId, String triggerTypeCode, String couponId
    ) {
        String issueRequestKey = memberId.concat(triggerTypeCode).concat(couponId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(issueRequestKey))) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "?????? ????????? ???????????????.");
        }
        redisTemplate.opsForValue().set(issueRequestKey, "", 10, TimeUnit.SECONDS);
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
        responseMessage.getCoupons()
                .forEach(coupon -> coupon.getCouponCodes()
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

    /**
     * ????????? ?????? ?????? ????????? ???????????? ?????? ?????? ??? ?????? ????????? ???????????????.
     *
     * @param requestDateTime ????????? ?????? ?????? ?????? ??????
     */
    private void checkMonthlyCouponIssueRequestTime(LocalDateTime requestDateTime) {
        if (Boolean.FALSE.equals(redisTemplate.opsForHash()
                .hasKey(MONTHLY_POLICY_KEY, MONTHLY_COUPON_OPEN_DATE_TIME_KEY))) {
            throw new ClientException(
                    ErrorCode.NOT_FOUND,
                    "Not found any monthly coupon open date time."
            );
        }

        String openDateTimeStr = redisTemplate.opsForHash()
                .get(MONTHLY_POLICY_KEY, MONTHLY_COUPON_OPEN_DATE_TIME_KEY)
                .toString();

        requestDateTime = requestDateTime.plusHours(9);     // UTC to KST
        LocalDateTime openDateTime = LocalDateTime.parse(
                openDateTimeStr,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );

        if (requestDateTime.isAfter(LocalDateTime.now())
                || requestDateTime.isBefore(openDateTime)) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Cannot process monthly coupon issue request before open time."
            );
        }
    }

    /**
     * ?????? ????????? ?????? ????????? ????????? ?????? ???????????? ???????????????.
     *
     * @param triggerTypeCode ????????? ????????? ????????? ?????? ??????
     * @return boolean
     */
    private boolean isMonthlyCoupon(TriggerTypeCode triggerTypeCode) {
        return triggerTypeCode.equals(TriggerTypeCode.COUPON_OF_THE_MONTH);
    }
}
