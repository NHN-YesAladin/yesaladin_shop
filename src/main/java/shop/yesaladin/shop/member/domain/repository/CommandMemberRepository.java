package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 등록 및 수정 관련 repository interface 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
public interface CommandMemberRepository {

    /**
     * 회원에 대한 데이터를 등록합니다.
     *
     * @param member 회원 데이터
     * @return 등록된 회원
     * @author 송학현
     * @since 1.0
     */
    Member save(Member member);
}
