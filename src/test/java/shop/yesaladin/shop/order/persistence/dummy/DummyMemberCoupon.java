package shop.yesaladin.shop.order.persistence.dummy;


import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.Member;

import java.time.LocalDate;

public class DummyMemberCoupon {

    public static MemberCoupon memberCoupon(Member member) {

        return MemberCoupon.builder()
                .member(member)
                .couponCode("")
                .couponGroupCode("")
                .isUsed(false)
                .expirationDate(LocalDate.of(2023, 1, 1))
                .build();
    }

}
