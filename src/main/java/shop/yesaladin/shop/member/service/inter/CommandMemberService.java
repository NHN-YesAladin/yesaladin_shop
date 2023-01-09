package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;

/**
 * Create, Update, Delete 를 Controller Layer에서 사용하기 위한 service interface
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
public interface CommandMemberService {

    /**
     * 회원을 등록하기 위한 기능 입니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 회원 등록 결과를 반환할 dto
     * @author : 송학현
     * @since : 1.0
     */
    MemberCreateResponseDto create(MemberCreateRequestDto createDto);

    /**
     * 회원 정보 수정을 위한 기능입니다.
     *
     * @param id        정보를 수정한 회원 id
     * @param updateDto 수정한 회원 정보 dto
     * @return 수정된 결과를 반환할 dto
     * @author 최예린
     * @since 1.0
     */
    MemberUpdateResponseDto update(Long id, MemberUpdateRequestDto updateDto);

    /**
     * 회원을 차단 하기 위한 기능입니다.
     *
     * @param id 차단할 회원 id
     * @author 최예린
     * @since 1.0
     */
    MemberBlockResponseDto block(Long id);

    /**
     * 회원 차단해지를 위한 기능 입니다.
     *
     * @param id 차단해지할 회원 id
     * @author 최예린
     * @since 1.0
     */
    MemberBlockResponseDto unblock(Long id);
}
