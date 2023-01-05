package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.Member;

public interface CommandMemberRepository {

    Member save(Member member);
}
