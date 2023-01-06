package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

/**
 * 회원 등급 이력 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandMemberGradeHistoryRepository {

    MemberGradeHistory save(MemberGradeHistory memberGradeHistory);
}
