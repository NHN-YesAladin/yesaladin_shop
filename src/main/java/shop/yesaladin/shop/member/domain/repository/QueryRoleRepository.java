package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.Role;

/**
 * 권한 조회 관련 repository interface 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
public interface QueryRoleRepository {

    /**
     * primary key를 통해 회원의 권한을 조회 합니다.
     *
     * @param id primary key
     * @return 조회된 회원 등급
     * @author 송학현
     * @since 1.0
     */
    Optional<Role> findById(Integer id);

}
