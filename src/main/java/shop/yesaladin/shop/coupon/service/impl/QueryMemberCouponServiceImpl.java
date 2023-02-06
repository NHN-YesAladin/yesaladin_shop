package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;

/**
 * 회원 쿠폰 조회와 관련한 서비스 구현체 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberCouponServiceImpl implements QueryMemberCouponService {

    private final QueryMemberCouponRepository queryMemberCouponRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberCoupon> findByCouponCodes(List<String> couponCodes) {
        List<MemberCoupon> memberCoupons = queryMemberCouponRepository.findByCouponCodes(couponCodes);

        checkAllCouponCodesAreAvailable(couponCodes, memberCoupons);

        return memberCoupons;
    }

    private void checkAllCouponCodesAreAvailable(
            List<String> couponCodes,
            List<MemberCoupon> memberCoupons
    ) {
        if (memberCoupons.size() != couponCodes.size()) {
            throw new ClientException(ErrorCode.COUPON_NOT_FOUND, "MemberCoupon not found.");
        }
    }


}
