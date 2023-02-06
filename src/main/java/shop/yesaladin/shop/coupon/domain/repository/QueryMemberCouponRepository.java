package shop.yesaladin.shop.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 멤버가 보유한 쿠폰을 조회하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberCouponRepository {

    boolean existsByMemberAndCouponGroupCodeList(String memberId, List<String> couponGroupCodeList);

    /**
     * 쿠폰 코드 리스트를 이용하여 회원의 쿠폰 리스트를 조회합니다.
     *
     * @param couponCodes 쿠폰 코드 리스트
     * @return 회원 쿠폰 리스트
     * @author 최예린
     * @since 1.0
     */
    List<MemberCoupon> findByCouponCodes(List<String> couponCodes);
}
