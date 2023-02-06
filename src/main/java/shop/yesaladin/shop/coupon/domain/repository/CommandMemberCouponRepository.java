package shop.yesaladin.shop.coupon.domain.repository;

import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 멤버가 보유한 쿠폰을 추가/삭제하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CommandMemberCouponRepository {

    MemberCoupon save(MemberCoupon memberCoupon);
}
