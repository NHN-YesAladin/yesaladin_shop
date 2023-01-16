package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;


/**
 * 회원 조회용 service interface
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
public interface QueryMemberService {

    /**
     * 회원을 primary key로 조회 하기 위한 메서드 입니다.
     *
     * @param id member의 primary key
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    MemberDto findMemberById(long id);

    /**
     * 회원을 unique column인 loginId를 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param loginId member의 loginId
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    MemberDto findMemberByLoginId(String loginId);

    /**
     * 회원을 unique column인 nickname을 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param nickname member의 nickname
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    MemberDto findMemberByNickname(String nickname);

    /**
     * 회원의 로그인 요청에 대해 유저 정보와 권한 정보를 함께 조회 하기 위한 메서드 입니다.
     *
     * @param loginId member의 loginId
     * @return login 대상의 유저 정보와 권한 정보를 담은 DTO
     * @author : 송학현
     * @since : 1.0
     */
    MemberLoginResponseDto findMemberLoginInfoByLoginId(String loginId);
}
