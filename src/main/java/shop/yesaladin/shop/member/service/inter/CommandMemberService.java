package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberEmailUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberNameUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberNicknameUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberPasswordUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberPhoneUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.dto.OauthMemberCreateRequestDto;

/**
 * Create, Update, Delete 를 Controller Layer에서 사용하기 위한 service interface
 *
 * @author 송학현, 최예린
 * @since 1.0
 */
public interface CommandMemberService {

    /**
     * 회원을 등록하기 위한 기능 입니다. 회원 등록 후 회원가입 쿠폰 요청 메시지를 발행합니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 회원 등록 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberCreateResponseDto create(MemberCreateRequestDto createDto);

    /**
     * OAuth2 회원을 등록하기 위한 기능 입니다. 회원 등록 후 회원가입 쿠폰 요청 메시지를 발행합니다.
     *
     * @param createDto OAuth2 회원 등록 요청 dto
     * @return 회원 등록 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberCreateResponseDto createOauth(OauthMemberCreateRequestDto createDto);

    /**
     * 회원의 nickname 수정을 위한 기능입니다.
     *
     * @param loginId 정보를 수정할 회원 아이디
     * @param request 수정할 회원의 nickname
     * @return 수정된 결과를 반환할 dto
     * @author 최예린
     * @author 송학현
     * @since 1.0
     */
    MemberUpdateResponseDto updateNickname(String loginId, MemberNicknameUpdateRequestDto request);

    /**
     * 회원의 이름을 수정을 위한 기능입니다.
     *
     * @param loginId 정보를 수정한 회원 아이디
     * @param request 수정할 회원의 이름
     * @return 수정된 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberUpdateResponseDto updateName(String loginId, MemberNameUpdateRequestDto request);

    /**
     * 회원의 전화번호를 수정하기 위한 기능입니다.
     *
     * @param loginId 정보를 수정한 회원 아이디
     * @param request 수정할 회원의 전화번호
     * @return 수정된 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberUpdateResponseDto updatePhone(String loginId, MemberPhoneUpdateRequestDto request);

    /**
     * 회원의 이메일을 수정하기 위한 기능입니다.
     *
     * @param loginId 정보를 수정한 회원 아이디
     * @param request 수정할 회원의 이메일
     * @return 수정된 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberUpdateResponseDto updateEmail(String loginId, MemberEmailUpdateRequestDto request);

    /**
     * 회원을 차단 하기 위한 기능입니다.
     *
     * @param loginId 차단할 회원 아이디
     * @param request 차단 사유
     * @author 최예린
     * @since 1.0
     */
    MemberBlockResponseDto block(String loginId, MemberBlockRequestDto request);

    /**
     * 회원 차단해지를 위한 기능 입니다.
     *
     * @param loginId 차단해지할 회원 아이디
     * @author 최예린
     * @since 1.0
     */
    MemberUnblockResponseDto unblock(String loginId);

    /**
     * 회원 탈퇴를 위한 기능 입니다.
     *
     * @param loginId 회원의 loginId
     * @return 회원 탈퇴 결과를 담은 DTO
     * @author 송학현
     * @since 1.0
     */
    MemberWithdrawResponseDto withDraw(String loginId);

    /**
     * 회원의 패스워드를 수정하기 위한 기능입니다.
     *
     * @param loginId 회원의 loginId
     * @param request 수정할 회원의 패스워드
     * @return 수정된 결과를 반환할 dto
     * @author 송학현
     * @since 1.0
     */
    MemberUpdateResponseDto updatePassword(String loginId, MemberPasswordUpdateRequestDto request);
}
