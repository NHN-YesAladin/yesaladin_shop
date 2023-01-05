package shop.yesaladin.shop.member.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.Member;

public interface QueryMemberRepository {

    List<Member> findAll();

    Optional<Member> findById(Long id);

}
