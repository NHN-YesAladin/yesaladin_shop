package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

public interface QueryMemberGradeRepository {

    Optional<MemberGrade> findById(Integer id);
}
