package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberRole;

/**
 * 회원과 권한의 관계 등록 및 수정 관련 repository interface 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
public interface CommandMemberRoleRepository {

    /**
     * 휘원 권한 관계 데이터를 등록합니다.
     *
     * @param memberRole 회원 권한 관계 데이터
     * @return 등록된 회원 권한 관계
     * @author 송학현
     * @since 1.0
     */
    MemberRole save(MemberRole memberRole);
}
