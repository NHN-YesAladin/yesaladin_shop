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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import shop.yesaladin.coupon.message.CouponResultDto;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.domain.model.CouponCodesMessage;
import shop.yesaladin.shop.coupon.domain.model.CouponGiveRequestMessage;
import shop.yesaladin.shop.coupon.domain.model.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.domain.model.CouponGivenResultMessage;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.model.ProcessingStatus;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원에게 쿠폰을 지급하는 서비스 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @author 서민지
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
    private final QueryMemberCouponRepository queryMemberCouponRepository;
    private final CommandMemberCouponRepository commandMemberCouponRepository;
    private final QueryMemberService queryMemberService;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final Clock clock;

    @Override
    @Transactional
    public RequestIdOnlyDto requestGiveCoupon(
            String memberId,
            TriggerTypeCode triggerTypeCode,
            Long couponId,
            LocalDateTime requestDateTime
    ) {
        if (isMonthlyCoupon(triggerTypeCode)) {
            checkMonthlyCouponIssueRequestTime(requestDateTime);
        }

        if (Objects.nonNull(couponId)) {    // 수동 발행 타입 쿠폰을 요청하는 경우
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

        sendGiveRequestMessage(triggerTypeCode, couponId, requestId);

        return new RequestIdOnlyDto(requestId);
    }

    @Override
    @Transactional
    public CouponResultDto giveCouponToMember(CouponGiveRequestResponseMessage responseMessage) {
        List<String> couponCodeList = responseMessage.getCoupons()
                .stream()
                .flatMap(coupon -> coupon.getCouponCodes().stream())
                .collect(Collectors.toList());

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

            tryGiveCouponToMember(responseMessage, memberId);

            sendGivenResultMessage(responseMessage.getRequestId(), new CouponCodesMessage(
                    null,
                    couponCodeList,
                    LocalDateTime.now(clock),
                    null,
                    ProcessingStatus.NOT_PROCESSED
            ), true);

            return new CouponResultDto(
                    CouponSocketRequestKind.GIVE,
                    responseMessage.getRequestId(),
                    responseMessage.isSuccess(),
                    responseMessage.isSuccess() ? "발급이 완료되었습니다."
                            : responseMessage.getErrorMessage(),
                    LocalDateTime.now(clock)
            );
        } catch (Exception e) {
            sendGivenResultMessage(responseMessage.getRequestId(), new CouponCodesMessage(
                    null,
                    couponCodeList,
                    LocalDateTime.now(clock),
                    null,
                    ProcessingStatus.NOT_PROCESSED
            ), false);

            return new CouponResultDto(
                    CouponSocketRequestKind.GIVE,
                    responseMessage.getRequestId(),
                    false,
                    responseMessage.getErrorMessage(),
                    LocalDateTime.now(clock)
            );
        }
    }


    /**
     * 쿠폰 발행 요청에 대한 정보를 10초 동안 redis 에 저장합니다. 10초 내에 같은 요청을 시도하는 경우 예외를 던집니다.
     *
     * @param memberId        발행 요청을 한 회원의 로그인 아이디
     * @param triggerTypeCode 발행 요청을 한 쿠폰의 트리거 타입 코드
     * @param couponId        발행 요청한 쿠폰의 아이디
     */
    public void registerIssueRequest(
            String memberId, String triggerTypeCode, String couponId
    ) {
        String issueRequestKey = memberId.concat(triggerTypeCode).concat(couponId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(issueRequestKey))) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "이미 처리된 요청입니다.");
        }
        redisTemplate.opsForValue().set(issueRequestKey, "", 10, TimeUnit.SECONDS);
    }

    private void sendGivenResultMessage(
            String requestId, CouponCodesMessage couponCodesMessage, boolean success
    ) {
        CouponGivenResultMessage resultMessage = new CouponGivenResultMessage(
                null,
                couponCodesMessage.getCouponCodes(),
                success,
                LocalDateTime.now(clock),
                null,
                ProcessingStatus.NOT_PROCESSED
        );

        markAsProcessed(requestId);

        mongoTemplate.insert(resultMessage);
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
            TriggerTypeCode triggerTypeCode, Long couponId, String requestId
    ) {
        CouponGiveRequestMessage giveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId(requestId)
                .triggerTypeCode(triggerTypeCode)
                .couponId(couponId)
                .createdDateTime(LocalDateTime.now(clock))
                .build();

        mongoTemplate.insert(giveRequestMessage);
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
     * 이달의 쿠폰 오픈 시간을 확인하여 오픈 시간 전 발행 요청을 처리합니다.
     *
     * @param requestDateTime 이달의 쿠폰 발행 요청 시간
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
     * 발행 요청의 쿠폰 타입이 이달의 쿠폰 타입인지 확인합니다.
     *
     * @param triggerTypeCode 요청한 쿠폰의 트리거 타입 코드
     * @return boolean
     */
    private boolean isMonthlyCoupon(TriggerTypeCode triggerTypeCode) {
        return triggerTypeCode.equals(TriggerTypeCode.COUPON_OF_THE_MONTH);
    }

    private void markAsProcessed(String requestId) {
        Query query = new Query(Criteria.where("_id").is(requestId));
        Update update = new Update();
        update.set("processed", ProcessingStatus.PROCESSING);
        update.set("updatedDateTime", LocalDateTime.now(clock));
        mongoTemplate.updateFirst(query, update, CouponGiveRequestResponseMessage.class);
    }
}
