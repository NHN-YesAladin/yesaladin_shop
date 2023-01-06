package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.Role;

/**
 * 권한 등록 및 수정 관련 repository interface 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandRoleRepository {

    /**
     * 권한에 대한 데이터를 등록합니다.
     *
     * @param role
     * @return
     * @author : 송학현
     * @since : 1.0
     */
    Role save(Role role);
}
