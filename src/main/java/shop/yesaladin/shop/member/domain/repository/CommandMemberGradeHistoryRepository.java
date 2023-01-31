package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

/**
 * 회원 등급 변경 이력 등록 및 수정 관련 repository interface 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
public interface CommandMemberGradeHistoryRepository {

    /**
     * 회원 등급 변경 이력에 대한 데이터를 등록합니다.
     *
     * @param memberGradeHistory 회원 등급 변경 이력 데이터
     * @return 등록된 회원 등급 변경 이력
     * @author 송학현
     * @since 1.0
     */
    MemberGradeHistory save(MemberGradeHistory memberGradeHistory);
}
