package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
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
     * 회원 정보 수정을 위한 기능입니다.
     *
     * @param loginId 정보를 수정한 회원 아이디
     * @param request 수정한 회원 정보 dto
     * @return 수정된 결과를 반환할 dto
     * @author 최예린
     * @since 1.0
     */
    MemberUpdateResponseDto update(String loginId, MemberUpdateRequestDto request);

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
}
