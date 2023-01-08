package shop.yesaladin.shop.member.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 조회 관련 repository interface 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberRepository {

    /**
     * primary key를 통해 회원을 조회 합니다.
     *
     * @param id primary key
     * @return 조회된 회원
     * @author : 송학현
     * @since : 1.0
     */
    Optional<Member> findById(Long id);

    /**
     * 회원의 nickname 을 통해 회원을 조회 합니다.
     *
     * @param nickname 회원의 nickname 입니다.
     * @return 조회된 회원
     * @author : 송학현
     * @since : 1.0
     */
    Optional<Member> findMemberByNickname(String nickname);

    /**
     * 회원의 login 시 사용 하는 ID를 통해 회원을 조회 합니다.
     *
     * @param loginId 회원의 loginId 입니다.
     * @return 조회된 회원
     * @author : 송학현
     * @since : 1.0
     */
    Optional<Member> findMemberByLoginId(String loginId);
}
