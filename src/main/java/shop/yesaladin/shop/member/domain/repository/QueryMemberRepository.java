package shop.yesaladin.shop.member.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;

/**
 * 회원 조회 관련 repository interface 입니다.
 *
 * @author 송학현
 * @author 최예린
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
     * 회원의 LoginId 를 통해 회원 Like 검색
     *
     * @param loginId 회원의 loginId
     * @param pageable 페이징 정보
     * @return 검색된 회원 리스트와 총 갯수
     * @author 김선홍
     * @since 1.0
     */
    Page<MemberManagerResponseDto> findMemberManagersByLoginId(String loginId, Pageable pageable);

    /**
     * 회원의 nickname 을 통해 회원 Like 검색
     *
     * @param nickname 회원의 nickname
     * @param pageable 페이징 정보
     * @return 검색된 회원 리스트와 총 갯수
     * @author 김선홍
     * @since 1.0
     */
    Page<MemberManagerResponseDto> findMemberManagersByNickname(String nickname, Pageable pageable);

    /**
     * 회원의 phone 를 통해 회원 Like 검색
     *
     * @param phone 회원의 phone 입니다.
     * @param pageable 페이징 정보
     * @return 검색된 회원 리스트와 총 갯수
     * @author 김선홍
     * @since 1.0
     */
    Page<MemberManagerResponseDto> findMemberManagersByPhone(String phone, Pageable pageable);


    /**
     * 회원의 name 을 통해 회원 Like 검색
     *
     * @param name   회원의 name 입니다.
     * @param pageable 페이징 정보
     * @return 검색된 회원 리스트와 총 갯수
     * @author 김선홍
     * @since 1.0
     */
    Page<MemberManagerResponseDto> findMemberManagersByName(String name, Pageable pageable);

    /**
     * 회원의 signUpDate 를 통해 회원 검색
     *
     * @param signUpDate 회원의 signUpDate 입니다.
     * @param pageable 페이징 정보
     * @return 검색된 회원 리스트와 총 갯수
     * @author 김선홍
     * @since 1.0
     */
    Page<MemberManagerResponseDto> findMemberManagersBySignUpDate(LocalDate signUpDate, Pageable pageable);

    /**
     * 회원의 birthMonth, birthDay 를 통해 회원을 조회 합니다.
     *
     * @param month 조회할 birthMonth
     * @param date  조회할 birthDay
     * @return 조회한 날짜가 생일인 회원 목록
     * @author 서민지
     * @since 1.0
     */
    List<MemberIdDto> findMemberIdsByBirthday(int month, int date);

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

    /**
     * 회원의 아이디로 주문서에 필요한 회원의 데이터를 조회합니다.
     *
     * @param loginId 회원 아이디
     * @return 주문서에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberOrderSheetResponseDto> getMemberOrderData(String loginId);

    /**
     * 전체 회원 수를 조회 합니다.
     *
     * @return 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countTotalMembers();

    /**
     * 전체 탈퇴 회원 수를 조회 합니다.
     *
     * @return 전체 탈퇴 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countWithdrawMembers();

    /**
     * 전체 차단 회원 수를 조회 합니다.
     *
     * @return 전체 차단 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countBlockedMembers();

    /**
     * 화이트 등급에 해당 하는 전체 회원 수를 조회 합니다.
     *
     * @return 화이트 등급에 해당 하는 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countWhiteMembers();

    /**
     * 브론즈 등급에 해당 하는 전체 회원 수를 조회 합니다.
     *
     * @return 브론즈 등급에 해당 하는 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countBronzeMembers();

    /**
     * 실버 등급에 해당 하는 전체 회원 수를 조회 합니다.
     *
     * @return 실버 등급에 해당 하는 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countSilverMembers();

    /**
     * 골드 등급에 해당 하는 전체 회원 수를 조회 합니다.
     *
     * @return 골드 등급에 해당 하는 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countGoldMembers();

    /**
     * 플래티넘 등급에 해당 하는 전체 회원 수를 조회 합니다.
     *
     * @return 플래티넘 등급에 해당 하는 전체 회원 수
     * @author 송학현
     * @since 1.0
     */
    Long countPlatinumMembers();
}
