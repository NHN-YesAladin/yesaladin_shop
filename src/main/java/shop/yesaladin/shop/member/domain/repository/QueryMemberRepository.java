package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.Member;

public interface QueryMemberRepository {

    Optional<Member> findById(Long id);

}
