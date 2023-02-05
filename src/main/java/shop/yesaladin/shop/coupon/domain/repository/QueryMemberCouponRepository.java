package shop.yesaladin.shop.coupon.domain.repository;

import java.util.List;

/**
 * 멤버가 보유한 쿠폰을 조회하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryMemberCouponRepository {

    boolean existsByMemberAndCouponGroupCodeList(String memberId, List<String> couponGroupCodeList);
}
