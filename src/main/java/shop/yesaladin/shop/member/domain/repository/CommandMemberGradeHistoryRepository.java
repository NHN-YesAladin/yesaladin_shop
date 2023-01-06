package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

public interface CommandMemberGradeHistoryRepository {

    MemberGradeHistory save(MemberGradeHistory memberGradeHistory);
}
