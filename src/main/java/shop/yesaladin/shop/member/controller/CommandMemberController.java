package shop.yesaladin.shop.member.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

/**
 * 회원에 관련된 RestController 입니다.
 *
 * @author : 송학현
 * @author 최예린
 * @since : 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class CommandMemberController {

    private final CommandMemberService commandMemberService;

    /**
     * 회원 가입을 위한 Post 요청을 처리 하는 기능 입니다.
     *
     * @param createDto 회원 가입을 위한 요청 파라미터의 모음입니다.
     * @return ResponseEntity로 회원 등록 성공 이후 등록된 일부 데이터들을 반환합니다.
     * @author : 송학현
     * @since : 1.0
     */
    @PostMapping
    public ResponseEntity<MemberCreateResponseDto> signUpMember(@Valid @RequestBody MemberCreateRequestDto createDto)
            throws URISyntaxException {
        MemberCreateResponseDto response = commandMemberService.create(createDto);

        return ResponseEntity.created(new URI(response.getId().toString())).body(response);
    }

    /**
     * 회원 정보 수정을 위한 Post 요청을 처리하는 기능입니다.
     *
     * @param updateDto 회원 정보 수정을 위한 요청 파라미터
     * @param loginId   회원의 아이디
     * @return 수정된 회원 정보를 담은 responseEntity
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{loginId}")
    @ResponseStatus(HttpStatus.OK)
    public MemberUpdateResponseDto updateMember(
            @Valid @RequestBody MemberUpdateRequestDto updateDto,
            @PathVariable String loginId
    ) {
        return commandMemberService.update(loginId, updateDto);
    }

    /**
     * 회원 차단을 위한 Put 요청을 처리하는 기능입니다.
     *
     * @param loginId 차단할 회원 아이디
     * @return 차단된 회원 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{loginId}/block")
    @ResponseStatus(HttpStatus.OK)
    public MemberBlockResponseDto blockMember(
            @PathVariable String loginId,
            @Valid @RequestBody MemberBlockRequestDto request
    ) {
        return commandMemberService.block(loginId, request);
    }

    /**
     * 회원 차단 해지를 위한 Put 요청을 처리하는 기능입니다.
     *
     * @param loginId 차단 해지할 회원 아이디
     * @return 차단 해지된 회원 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{loginId}/unblock")
    @ResponseStatus(HttpStatus.OK)
    public MemberUnblockResponseDto unblockMember(@PathVariable String loginId) {
        return commandMemberService.unblock(loginId);
    }

    /**
     * 회원 탈퇴를 위한 Delete 요청을 처리하는 기능입니다.
     *
     * @param loginId 회원 탈퇴 대상 loginId
     * @return 회원 탈퇴 결과
     * @author : 송학현
     * @since : 1.0
     */
    @DeleteMapping("/withdraw/{loginId}")
    @ResponseStatus(HttpStatus.OK)
    public MemberWithdrawResponseDto deleteMember(@PathVariable String loginId) {
        log.info("request={}",loginId);
        return commandMemberService.withDraw(loginId);
    }
}
