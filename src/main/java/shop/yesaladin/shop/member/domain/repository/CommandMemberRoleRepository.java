package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberRole;

/**
 * 회원 권한 관계 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandMemberRoleRepository {

    MemberRole save(MemberRole memberRole);
}
