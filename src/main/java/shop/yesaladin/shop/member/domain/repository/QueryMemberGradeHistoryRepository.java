package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

/**
 * 회원 등급 이력 조회 관련 repository 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberGradeHistoryRepository {

    Optional<MemberGradeHistory> findById(Long id);
}
