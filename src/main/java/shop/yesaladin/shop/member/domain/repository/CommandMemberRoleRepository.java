package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberRole;

public interface CommandMemberRoleRepository {

    MemberRole save(MemberRole memberRole);
}
