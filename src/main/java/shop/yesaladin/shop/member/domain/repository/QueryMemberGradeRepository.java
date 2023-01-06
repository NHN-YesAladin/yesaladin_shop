package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급 조회 관련 repository 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberGradeRepository {

    Optional<MemberGrade> findById(Integer id);
}
