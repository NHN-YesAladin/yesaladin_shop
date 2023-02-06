package shop.yesaladin.shop.coupon.service.inter;

import java.util.List;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 회원 쿠폰 조회와 관련한 서비스 interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberCouponService {

    /**
     * 쿠폰의 코드를 통해 회원 쿠폰을 조회합니다.
     *
     * @param couponCodes 쿠폰 코드 리스트
     * @return 회원의 쿠폰 리스트
     * @author 최예린
     * @since 1.0
     */
    List<MemberCoupon> findByCouponCodes(List<String> couponCodes);
}
