package shop.yesaladin.shop.member.dummy;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;
import shop.yesaladin.shop.member.domain.model.Role;

public class MemberRoleDummy {

    public static MemberRole dummy() {
        long memberId = 1L;
        int roleId = 1;

        return MemberRole.builder()
                .id(new Pk(memberId, roleId))
                .member(MemberDummy.dummy())
                .role(RoleDummy.dummy())
                .build();
    }

    public static MemberRole dummy(Member member, Role role) {
        return MemberRole.builder()
                .id(new Pk(member.getId(), role.getId()))
                .member(member)
                .role(role)
                .build();
    }
}
