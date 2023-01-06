package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;

public interface QueryMemberRoleRepository {

    Optional<MemberRole> findById(Pk id);
}
