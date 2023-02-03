package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

public class DummyMemberAddress {

    public static MemberAddress address(Member member) {
        return MemberAddress.builder()
                .address("서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)")
                .isDefault(true)
                .isDeleted(false)
                .member(member)
                .build();
    }

    public static MemberAddress addressWithId(Member member) {
        return MemberAddress.builder()
                .id(1L)
                .address("서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)")
                .isDefault(true)
                .isDeleted(false)
                .member(member)
                .build();
    }
}
