package shop.yesaladin.shop.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
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
 * @author 송학현
 * @author 최예린
 * @since 1.0
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
     * @author 송학현
     * @since 1.0
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MemberCreateResponseDto> signUpMember(@Valid @RequestBody MemberCreateRequestDto createDto) {
        MemberCreateResponseDto response = commandMemberService.create(createDto);
        return ResponseDto.<MemberCreateResponseDto>builder()
                .status(HttpStatus.CREATED)
                .success(true)
                .data(response)
                .build();
    }

    /**
     * 회원 정보 수정을 위한 Post 요청을 처리하는 기능입니다.
     *
     * @param updateDto     회원 정보 수정을 위한 요청 파라미터
     * @param bindingResult 유효성 검사
     * @param loginId       회원의 아이디
     * @return 수정된 회원 정보를 담은 responseEntity
     * @author 최예린
     * @since 1.0
     */
    @PutMapping
    public ResponseDto<MemberUpdateResponseDto> updateMember(
            @Valid @RequestBody MemberUpdateRequestDto updateDto,
            BindingResult bindingResult,
            @LoginId(required = true) String loginId
    ) {
        checkRequestValidation(bindingResult);

        MemberUpdateResponseDto response = commandMemberService.update(loginId, updateDto);

        return ResponseDto.<MemberUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 회원 차단을 위한 Put 요청을 처리하는 기능입니다.
     *
     * @param loginId        차단할 회원의 아이디
     * @param request        회원 차단 사유
     * @param bindingResult  유효성 검사
     * @return 차단된 회원 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("{loginId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto<MemberBlockResponseDto> blockMember(
            @PathVariable String loginId,
            @Valid @RequestBody MemberBlockRequestDto request,
            BindingResult bindingResult
    ) {
        checkRequestValidation(bindingResult);

        MemberBlockResponseDto response = commandMemberService.block(loginId, request);

        return ResponseDto.<MemberBlockResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 회원 차단 해지를 위한 Put 요청을 처리하는 기능입니다.
     *
     * @param loginId 차단 해지할 회원의 아이디
     * @return 차단 해지된 회원 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{loginId}/unblock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto<MemberUnblockResponseDto> unblockMember(
            @PathVariable String loginId
    ) {
        MemberUnblockResponseDto response = commandMemberService.unblock(loginId);

        return ResponseDto.<MemberUnblockResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation Error in member request."
            );
        }
    }

    /**
     * 회원 탈퇴를 위한 Delete 요청을 처리하는 기능입니다.
     *
     * @param loginId 회원 탈퇴 대상 loginId
     * @return 회원 탈퇴 결과
     * @author 송학현
     * @since 1.0
     */
    @DeleteMapping("/withdraw/{loginId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberWithdrawResponseDto> deleteMember(@PathVariable String loginId) {
        log.info("request={}", loginId);
        MemberWithdrawResponseDto response = commandMemberService.withDraw(loginId);
        return ResponseDto.<MemberWithdrawResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(response)
                .build();
    }
}
