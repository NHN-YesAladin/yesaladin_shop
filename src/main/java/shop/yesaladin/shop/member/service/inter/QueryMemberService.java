package shop.yesaladin.shop.member.service.inter;

import java.time.LocalDate;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerListResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;


/**
 * 회원 조회용 service interface
 *
 * @author 송학현
 * @author 최예린
 * @author 김선홍
 * @since 1.0
 */
public interface QueryMemberService {

    /**
     * 회원을 primary key로 조회 하기 위한 메서드 입니다.
     *
     * @param id member의 primary key
     * @return 회원 조회 결과
     * @author 송학현
     * @author 최예린
     * @since 1.0
     */
    MemberDto findMemberById(long id);

    /**
     * 회원을 unique column인 loginId를 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param loginId member의 loginId
     * @return 회원 조회 결과
     * @author 송학현
     * @author 최예린
     * @since 1.0
     */
    MemberDto findMemberByLoginId(String loginId);

    /**
     * 회원을 unique column인 nickname을 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param nickname member의 nickname
     * @return 회원 조회 결과
     * @author 송학현
     * @author 최예린
     * @since 1.0
     */
    MemberDto findMemberByNickname(String nickname);

    /**
     * 회원의 로그인 요청에 대해 유저 정보와 권한 정보를 함께 조회 하기 위한 메서드 입니다.
     *
     * @param loginId member의 loginId
     * @return login 대상의 유저 정보와 권한 정보를 담은 DTO
     * @author 송학현
     * @since 1.0
     */
    MemberLoginResponseDto findMemberLoginInfoByLoginId(String loginId);

    /**
     * 관리자의 회원 관리 요창에 대해 회원을 unique column 인 LoginId 를 기준으로 조회하는 메서드 이다.
     *
     * @param loginId member의 loginId
     * @return loginId 를 가지는 회원의 정보를 담은 DTO
     * @author 김선홍
     * @since 1.0
     */
    MemberManagerResponseDto findMemberManageByLoginId(String loginId);

    /**
     * 관리자의 회원 관리 요창에 대해 회원을 unique column 인 nickname 를 기준으로 조회하는 메서드 이다.
     *
     * @param nickname member의 nickname
     * @return nickname 을 가지는 회원의 정보를 담은 DTO
     * @author 김선홍
     * @since 1.0
     */
    MemberManagerResponseDto findMemberManageByNickName(String nickname);

    /**
     * 관리자의 회원 관리 요창에 대해 회원을 unique column 인 phone 를 기준으로 조회하는 메서드 이다.
     *
     * @param phone member의 loginId
     * @return phone 를 가지는 회원의 정보를 담은 DTO
     * @author 김선홍
     * @since 1.0
     */
    MemberManagerResponseDto findMemberManageByPhone(String phone);

    /**
     * 관리자의 회원 관리 요창에 대해 회원을 column 인 name 을 기준으로 조회하는 메서드 이다.
     *
     * @param name member의 name
     * @return name 을 가지는 회원의 정보를 담은 DTO 리스트
     * @author 김선홍
     * @since 1.0
     */
    MemberManagerListResponseDto findMemberManageByName(String name, int offset, int limit);

    /**
     * 관리자의 회원 관리 요창에 대해 회원을 column 인 signUpDate 를 기준으로 조회하는 메서드 이다.
     *
     * @param signUpDate member의 signUpDate
     * @return signUpDate 를 가지는 회원의 정보를 담은 DTO 리스트
     * @author 김선홍
     * @since 1.0
     */
    MemberManagerListResponseDto findMemberManageBySignUpDate(LocalDate signUpDate, int offset, int limit);

    /**
     * 회원 가입 시 입력할 loginId를 사전에 중복 판별을 하기 위한 메서드 입니다.
     *
     * @param loginId 중복 체크 대상 loginId
     * @return loginId의 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsLoginId(String loginId);

    /**
     * 회원 가입 시 입력할 nickname을 사전에 중복 판별을 하기 위한 메서드 입니다.
     *
     * @param nickname 중복 체크 대상 nickname
     * @return nickname의 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsNickname(String nickname);

    /**
     * 회원 가입 시 입력할 email을 사전에 중복 판별을 하기 위한 메서드 입니다.
     *
     * @param email 중복 체크 대상 email
     * @return email의 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsEmail(String email);

    /**
     * 회원 가입 시 입력할 phone을 사전에 중복 판별을 하기 위한 메서드 입니다.
     *
     * @param phone 중복 체크 대상 phone
     * @return phone의 존재 유무
     * @author 송학현
     * @since 1.0
     */
    boolean existsPhone(String phone);

    /**
     * 회원의 아이디로 회원의 등급을 반환합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 등급
     * @author 최예린
     * @since 1.0
     */
    MemberGradeQueryResponseDto getMemberGrade(String loginId);

    /**
     * 회원의 정보를 가져옵니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원 정보
     * @author 최예린
     * @since 1.0
     */
    MemberQueryResponseDto getByLoginId(String loginId);
}
