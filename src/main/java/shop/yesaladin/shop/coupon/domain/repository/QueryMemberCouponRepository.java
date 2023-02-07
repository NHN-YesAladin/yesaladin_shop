package shop.yesaladin.shop.coupon.domain.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 멤버가 보유한 쿠폰을 조회하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberCouponRepository {

    /**
     * 멤버가 인자로 넘기는 그룹 코드를 가진 쿠폰을 이미 가지고 있는지 확인합니다.
     *
     * @param memberId            확인할 멤버 아이디
     * @param couponGroupCodeList 그룹 코드 리스트
     * @return 인자로 넘어간 그룹 코드를 가진 쿠폰 보유 여부
     */
    boolean existsByMemberAndCouponGroupCodeList(String memberId, List<String> couponGroupCodeList);

    /**
     * 회원의 로그인 아이디로 회원을 쿠폰 목록을 가져옵니다.
     *
     * @param memberId 회원의 로그인 아이디
     * @param usable 사용 가능 여부
     * @return 회원이 가진 쿠폰 리스트
     */
    Page<MemberCoupon> findMemberCouponByMemberId(Pageable pageable, String memberId,
            boolean usable
    );

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
