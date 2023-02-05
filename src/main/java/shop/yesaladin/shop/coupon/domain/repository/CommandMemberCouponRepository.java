package shop.yesaladin.shop.coupon.domain.repository;

import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

public interface CommandMemberCouponRepository {

    MemberCoupon save(MemberCoupon memberCoupon);
}
