package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급 조회용 service interface
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberGradeService {

    /**
     * 회원 등급을 조회 하기 위한 메서드 입니다.
     *
     * @param id 회원 등급의 primary key
     * @return 회원의 등급 조회 결과
     * @author : 송학현
     * @since : 1.0
     */
    MemberGrade findById(int id);
}
