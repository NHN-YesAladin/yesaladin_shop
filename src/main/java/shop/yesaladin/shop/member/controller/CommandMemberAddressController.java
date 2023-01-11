package shop.yesaladin.shop.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressUpdateResponseDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

/**
 * 회원지 배송과 관련한 등록/수정/삭제 Rest Controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members/{memberId}/addresses")
public class CommandMemberAddressController {

    private final CommandMemberAddressService commandMemberAddressService;

    /**
     * 회원지 배송 등록을 위한 기능입니다.
     *
     * @param memberId 회원 id
     * @param request  등록할 배송지 데이터
     * @return 등록된 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberAddressCreateResponseDto createMemberAddress(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberAddressCreateRequestDto request
    ) {
        return commandMemberAddressService.save(memberId, request);
    }

    /**
     * 회원의 배송지를 대표 배송지로 설정하는 기능입니다.
     *
     * @param memberId  회원 id
     * @param addressId 대표로 지정할 배송지 id
     * @return 회원의 대표 배송지 정보
     * @author 최예린
     * @since 1.0
     */
    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public MemberAddressUpdateResponseDto markAsDefaultAddress(
            @PathVariable Long memberId,
            @PathVariable Long addressId
    ) {
        return commandMemberAddressService.markAsDefault(memberId, addressId);
    }

    /**
     * 회원의 배송지를 삭제하는 기능입니다.
     *
     * @param memberId  회원 id
     * @param addressId 삭제할 배송 id
     * @author 최예린
     * @since 1.0
     */
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberAddress(@PathVariable Long memberId, @PathVariable Long addressId) {
        commandMemberAddressService.delete(memberId, addressId);
    }
}
