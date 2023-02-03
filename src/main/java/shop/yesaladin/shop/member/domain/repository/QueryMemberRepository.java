package shop.yesaladin.shop.member.domain.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 조회 관련 repository interface 입니다.
 *
 * @author 송학현
 * @author 김선홍
 * @since 1.0
 */
public interface QueryMemberRepository {

    /**
     * primary key를 통해 회원을 조회 합니다.
     *
     * @param id primary key
     * @return 조회된 회원
     * @author 송학현
     * @since 1.0
     */
    Optional<Member> findById(Long id);

    /**
     * 회원의 nickname 을 통해 회원을 조회 합니다.
     *
     * @param nickname 회원의 nickname 입니다.
     * @return 조회된 회원
     * @author 송학현
     * @since 1.0
     */
    Optional<Member> findMemberByNickname(String nickname);

    /**
     * 회원의 login 시 사용 하는 ID를 통해 회원을 조회 합니다.
     *
     * @param loginId 회원의 loginId 입니다.
     * @return 조회된 회원
     * @author 송학현
     * @since 1.0
     */
    Optional<Member> findMemberByLoginId(String loginId);

    /**
     * 회원의 email 을 통해 회원을 조회 합니다.
     *
     * @param email 회원의 email 입니다.
     * @return 조회된 회원
     * @author 송학현
     * @since 1.0
     */
    Optional<Member> findMemberByEmail(String email);

    /**
     * 회원의 phone 를 통해 회원을 조회 합니다.
     *
     * @param phone 회원의 phone 입니다.
     * @return 조회된 회원
     * @author 김선홍
     * @since 1.0
     */
    Optional<Member> findMemberByPhone(String phone);

    /**
     * 회원의 name 을 통해 회원을 조회합니다.
     *
     * @param name 회원의 name 입니다.
     * @param offset 페이지 위치
     * @param limit 데이터 갯수
     * @return 조회된 회원
     */
    Page<Member> findMembersByName(String name, int offset, int limit);

    /**
     * 회원의 signUpDate 를 통해 회원을 조회합니다.
     *
     * @param signUpDate 회원의 signUpDate 입니다.
     * @param offset 페이지 위치
     * @param limit 데이터 갯수
     * @return 조회된 회원
     */
    Page<Member> findMembersBySignUpDate(LocalDate signUpDate, int offset, int limit);

    /**
     * 회원의 loginId 을 통해 회원이 존재 하는지 유무를 판별합니다.
     *
     * @param loginId 회원의 loginId 입니다.
     * @return 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsMemberByLoginId(String loginId);

    /**
     * 회원의 nickname 을 통해 회원이 존재 하는지 유무를 판별합니다.
     *
     * @param nickname 회원의 nickname 입니다.
     * @return 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsMemberByNickname(String nickname);

    /**
     * 회원의 email 을 통해 회원이 존재 하는지 유무를 판별합니다.
     *
     * @param email 회원의 email 입니다.
     * @return 존재 유무
     * @author : 송학현
     * @since : 1.0
     */
    boolean existsMemberByEmail(String email);

    /**
     * 회원의 phone 을 통해 회원이 존재 하는지 유무를 판별합니다.
     *
     * @param phone 회원의 phone 입니다.
     * @return 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsMemberByPhone(String phone);
}
