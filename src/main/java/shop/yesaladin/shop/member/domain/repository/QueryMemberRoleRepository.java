package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;

/**
 * 멤버 권한 관계 테이블 조회 관련 repository 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberRoleRepository {

    Optional<MemberRole> findById(Pk pk);
}
