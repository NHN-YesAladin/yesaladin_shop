package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;

/**
 * 권한과 회원의 관계 테이블 조회 관련 repository interface 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberRoleRepository {

    /**
     * primary key를 통해 회원과 권한의 관계 테이블을 조회 합니다.
     *
     * @param pk primary key
     * @return 조회된 회원 등급
     * @author : 송학현
     * @since : 1.0
     */
    Optional<MemberRole> findById(Pk pk);
}
