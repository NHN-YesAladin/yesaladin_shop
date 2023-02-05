package shop.yesaladin.shop.member.domain.repository;

import java.util.List;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

/**
 * 회원 쿠폰을 조회하는 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberCouponRepository {

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
