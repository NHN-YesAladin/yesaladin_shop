package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class DummyMemberGrade {

    public static MemberGrade memberGrade = MemberGrade.builder()
            .id(1)
            .name("silver")
            .baseOrderAmount(20000L)
            .baseGivenPoint(1000L).build();
}
