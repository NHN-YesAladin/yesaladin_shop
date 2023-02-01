package shop.yesaladin.shop.member.service.inter;

import java.util.List;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

public interface CommandMemberCouponService {

    void createMemberCoupons(List<MemberCoupon> memberCouponList);
}
