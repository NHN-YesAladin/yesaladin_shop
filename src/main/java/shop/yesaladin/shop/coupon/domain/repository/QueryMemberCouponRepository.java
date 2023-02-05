package shop.yesaladin.shop.coupon.domain.repository;

import java.util.List;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 멤버가 보유한 쿠폰을 조회하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryMemberCouponRepository {

    /**
     * 멤버가 인자로 넘기는 그룹 코드를 가진 쿠폰을 이미 가지고 있는지 확인합니다.
     * @param memberId 확인할 멤버 아이디
     * @param couponGroupCodeList 그룹 코드 리스트
     * @return 인자로 넘어간 그룹 코드를 가진 쿠폰 보유 여부
     */
    boolean existsByMemberAndCouponGroupCodeList(String memberId, List<String> couponGroupCodeList);

    /**
     * 회원의 로그인 아이디로 회원을 쿠폰 목록을 가져옵니다.
     * @param memberId 회원의 로그인 아이디
     * @return 회원이 가진 쿠폰 리스트
     */
    List<MemberCoupon> findMemberCouponByMemberId(String memberId);
}
