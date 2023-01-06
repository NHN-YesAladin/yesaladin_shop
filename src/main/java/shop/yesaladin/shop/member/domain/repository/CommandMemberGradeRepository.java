package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급 등록 및 수정 관련 repository interface 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandMemberGradeRepository {

    /**
     * 회원 등급에 대한 데이터를 등록합니다.
     *
     * @param memberGrade 회원 등급 데이터
     * @return 등록된 회원 등급
     * @author : 송학현
     * @since : 1.0
     */
    MemberGrade save(MemberGrade memberGrade);
}
