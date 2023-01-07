package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급 조회용 service interface
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberGradeService {

    MemberGrade findById(int id);
}
