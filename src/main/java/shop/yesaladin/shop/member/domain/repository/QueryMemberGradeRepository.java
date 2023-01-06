package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급 조회 관련 repository interface 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberGradeRepository {

    /**
     * primary key를 통해 회원의 등급을 조회 합니다.
     *
     * @param id primary key
     * @return 조회된 회원 등급
     * @author : 송학현
     * @since : 1.0
     */
    Optional<MemberGrade> findById(Integer id);
}
