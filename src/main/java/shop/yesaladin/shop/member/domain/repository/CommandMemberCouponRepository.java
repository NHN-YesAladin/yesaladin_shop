package shop.yesaladin.shop.member.domain.repository;

import java.util.List;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

public interface CommandMemberCouponRepository {

    List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons);
}
