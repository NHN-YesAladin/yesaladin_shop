package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.Role;

public interface QueryRoleRepository {

    Optional<Role> findById(Integer id);

}
