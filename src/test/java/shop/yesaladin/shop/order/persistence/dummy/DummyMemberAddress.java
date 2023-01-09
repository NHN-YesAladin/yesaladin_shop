package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

public class DummyMemberAddress {

    public static MemberAddress address(Member member) {
        return MemberAddress.builder()
                .id(1L)
                .address("Gwang Ju")
                .isDefault(true)
                .member(member)
                .build();
    }
}
