package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.Role;

public interface CommandRoleRepository {

    Role save(Role role);
}
