package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponCodeOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;

/**
 * 쿠폰 사용을 위한 서비스 인터페이스의 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UseCouponServiceImpl implements UseCouponService {

    private final QueryMemberCouponRepository queryMemberCouponRepository;
    private final CouponProducer couponProducer;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<CouponCodeOnlyDto> useCoupon(CouponUseRequestResponseMessage message) {
        if (!message.isSuccess()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Cannot use coupons. Request id : " + message.getRequestId()
            );
        }

        List<String> couponCodeList = getUsedCouponCode(message);

        try {
            List<MemberCoupon> memberCouponList = queryMemberCouponRepository.findByCouponCodes(
                    couponCodeList);

            memberCouponList.forEach(MemberCoupon::use);

            sendUseResultMessage(couponCodeList, true);

            return couponCodeList.stream().map(CouponCodeOnlyDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            sendUseResultMessage(couponCodeList, false);

            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Use coupon failed. Coupon codes : " + couponCodeList
            );
        }
    }

    private List<String> getUsedCouponCode(CouponUseRequestResponseMessage message) {
        List<String> couponCodeList = redisTemplate.opsForList()
                .range(message.getRequestId(), 0, -1);
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
