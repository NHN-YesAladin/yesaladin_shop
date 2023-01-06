package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

public interface CommandMemberGradeRepository {

    MemberGrade save(MemberGrade memberGrade);
}
