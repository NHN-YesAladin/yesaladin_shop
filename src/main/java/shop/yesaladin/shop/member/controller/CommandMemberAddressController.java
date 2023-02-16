package shop.yesaladin.shop.member.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

/**
 * 회원지 배송과 관련한 등록/수정/삭제 Rest Controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member-addresses")
public class CommandMemberAddressController {

    private final CommandMemberAddressService commandMemberAddressService;

    /**
     * 회원지 배송 등록을 위한 기능입니다.
     *
     * @param request 등록할 배송지 데이터
     * @param loginId 회원의 아이디
     * @return 등록된 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MemberAddressResponseDto> createMemberAddress(
            @Valid @RequestBody MemberAddressCreateRequestDto request,
            BindingResult bindingResult,
            @LoginId(required = true) String loginId
    ) {
        checkRequestValidation(bindingResult);

        MemberAddressResponseDto response = commandMemberAddressService.save(loginId, request);

        return ResponseDto.<MemberAddressResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response).build();
    }

    /**
     * 회원의 배송지를 대표 배송지로 설정하는 기능입니다.
     *
     * @param addressId 대표로 지정할 배송지 id
     * @param loginId   회원의 아이디
     * @return 회원의 대표 배송지 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{addressId}")
    public ResponseDto<MemberAddressResponseDto> markAsDefaultAddress(
            @PathVariable Long addressId,
            @LoginId(required = true) String loginId
    ) {
        MemberAddressResponseDto response = commandMemberAddressService.markAsDefault(
                loginId,
                addressId
        );

        return ResponseDto.<MemberAddressResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 회원의 배송지를 삭제하는 기능입니다.
     *
     * @param addressId 삭제할 배송 id
     * @param loginId   회원의 아이디
     * @author 최예린
     * @since 1.0
     */
    @DeleteMapping("/{addressId}")
    public ResponseDto<Void> deleteMemberAddress(
            @PathVariable Long addressId,
            @LoginId(required = true) String loginId
    ) {
        commandMemberAddressService.delete(loginId, addressId);

        return ResponseDto.<Void>builder().success(true).status(HttpStatus.OK).build();
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation Error in member address create request." +
                            bindingResult.getAllErrors()
            );
        }
    }

    @ExceptionHandler(ClientException.class)
    @ResponseBody
    public ResponseDto<Void> clientExceptionHandler(ClientException ce) {
        return ResponseDto.<Void>builder()
                .success(true)
                .errorMessages(List.of(ce.getDisplayErrorMessage()))
                .status(ce.getResponseStatus())
                .build();
    }
}
