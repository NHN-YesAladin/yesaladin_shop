package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

public interface QueryMemberGradeHistoryRepository {

    Optional<MemberGradeHistory> findById(Integer id);
}
