package shop.yesaladin.shop.coupon.service.impl;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.coupon.code.CouponSocketRequestKind;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponResultDto;
import shop.yesaladin.coupon.message.CouponUseRequestMessage;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponCodeOnlyDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.event.CouponRequestProcessEndEvent;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

/**
 * 쿠폰 사용을 위한 서비스 인터페이스의 구현체입니다.
 *
 * @author 김홍대, 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UseCouponServiceImpl implements UseCouponService {

    private static final String COUPON_CODE_SUFFIX = "-codes";
    private final QueryMemberCouponRepository queryMemberCouponRepository;
    private final CouponProducer couponProducer;
    private final QueryMemberCouponService queryMemberCouponService;
    private final CommandPointHistoryService commandPointHistoryService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final Clock clock;


    /**
     * {@inheritDoc}
     */
    @Override
    public RequestIdOnlyDto sendCouponUseRequest(String memberId, List<String> couponCodeList) {
        List<MemberCoupon> memberCouponList = queryMemberCouponRepository.findByCouponCodes(
                couponCodeList);

        if (!canUseAllCoupon(memberCouponList)) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Cannot use coupon. Requested coupon code list : " + couponCodeList
            );
        }

        String requestId = generateRequestId(memberId);
        saveRequestedCouponCodeList(couponCodeList, requestId);

        CouponUseRequestMessage requestMessage = new CouponUseRequestMessage(
                requestId,
                couponCodeList,
                LocalDateTime.now(clock)
        );

        couponProducer.produceUseRequestMessage(requestMessage);

        return new RequestIdOnlyDto(requestId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<CouponCodeOnlyDto> useCoupon(CouponUseRequestResponseMessage message) {
        List<String> couponCodeList = getUsedCouponCode(message);

        if (!message.isSuccess()) {
            publishCouponRequestProcessEndEvent(message);
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Cannot use coupons. Request id : " + message.getRequestId() + ". "
                            + message.getErrorMessage()
            );
        }

        try {
            List<MemberCouponSummaryDto> couponSummaryDtoList = queryMemberCouponService.getMemberCouponSummaryListByCouponCode(
                    couponCodeList);
            checkCouponIsPointCouponType(couponSummaryDtoList, message.getRequestId());

            List<MemberCoupon> memberCouponList = queryMemberCouponRepository.findByCouponCodes(
                    couponCodeList);

            memberCouponList.forEach(MemberCoupon::use);

            sendUseResultMessage(couponCodeList, true);

            publishCouponRequestProcessEndEvent(message);

            return couponCodeList.stream().map(CouponCodeOnlyDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            sendUseResultMessage(couponCodeList, false);
            publishCouponRequestProcessEndEvent(message, false);
            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Use coupon failed. Coupon codes : " + couponCodeList
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CouponCodeOnlyDto> cancelCouponUse(List<String> couponCodeList) {
        couponProducer.produceUseRequestCancelMessage(new CouponCodesMessage(couponCodeList));

        return couponCodeList.stream().map(CouponCodeOnlyDto::new).collect(Collectors.toList());
    }

    private void publishCouponRequestProcessEndEvent(CouponUseRequestResponseMessage message) {
        this.publishCouponRequestProcessEndEvent(message, message.isSuccess());
    }

    private void publishCouponRequestProcessEndEvent(
            CouponUseRequestResponseMessage message, boolean success
    ) {
        CouponResultDto resultMessage = new CouponResultDto(
                CouponSocketRequestKind.USE,
                message.getRequestId(),
                success,
                success ? "사용이 완료되었습니다." : message.getErrorMessage(),
                LocalDateTime.now(clock)
        );
        eventPublisher.publishEvent(new CouponRequestProcessEndEvent(this, resultMessage));
    }

    private void checkCouponIsPointCouponType(
            List<MemberCouponSummaryDto> couponSummaryDtoList, String requestId
    ) {
        boolean isAllPointCouponType = couponSummaryDtoList.stream()
                .allMatch(memberCouponSummaryDto -> memberCouponSummaryDto.getCouponTypeCode()
                        .equals(CouponTypeCode.POINT));

        if (couponSummaryDtoList.isEmpty() || isAllPointCouponType) {
            String memberId = getMemberIdByRequestId(requestId);
            chargeMemberPoint(memberId, couponSummaryDtoList);
        }
    }

    private void chargeMemberPoint(
            String memberId, List<MemberCouponSummaryDto> couponSummaryDtoList
    ) {
        for (MemberCouponSummaryDto memberCouponSummaryDto : couponSummaryDtoList) {
            commandPointHistoryService.save(new PointHistoryRequestDto(
                    memberId,
                    memberCouponSummaryDto.getAmount(),
                    PointReasonCode.SAVE_COUPON
            ));
        }
    }

    private boolean canUseAllCoupon(List<MemberCoupon> memberCouponList) {
        return memberCouponList.stream().noneMatch(MemberCoupon::isUsed);
    }

    private String generateRequestId(String memberId) {
        String requestId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(requestId, memberId, Duration.ofMinutes(30));
        return requestId;
    }

    private Long saveRequestedCouponCodeList(List<String> couponCodeList, String requestId) {
        return redisTemplate.opsForList()
                .rightPushAll(requestId + COUPON_CODE_SUFFIX, couponCodeList);
    }

    private String getMemberIdByRequestId(String requestId) {
        return redisTemplate.opsForValue().get(requestId);
    }

    private List<String> getUsedCouponCode(CouponUseRequestResponseMessage message) {
        String key = message.getRequestId() + COUPON_CODE_SUFFIX;
        List<String> couponCodeList = redisTemplate.opsForList().range(key, 0, -1);
        redisTemplate.opsForList().getOperations().delete(key);
        if (Objects.isNull(couponCodeList)) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Request id not exists or expired. Request id : " + message.getRequestId()
            );
        }
        return couponCodeList;
    }

    private void sendUseResultMessage(List<String> couponCodeList, boolean success) {
        CouponCodesAndResultMessage couponUseResultMessage = CouponCodesAndResultMessage.builder()
                .couponCodes(couponCodeList)
                .success(success)
                .build();

        couponProducer.produceUsedResultMessage(couponUseResultMessage);
    }

}
