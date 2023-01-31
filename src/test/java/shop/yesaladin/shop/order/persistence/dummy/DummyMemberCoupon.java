package shop.yesaladin.shop.order.persistence.dummy;


import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

public class DummyMemberCoupon {

    public static MemberCoupon memberCoupon(Member member) {

        return MemberCoupon.builder().member(member).couponCode("").couponGroupCode("").build();
    }

}
