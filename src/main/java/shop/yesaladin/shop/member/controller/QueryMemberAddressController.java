package shop.yesaladin.shop.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

/**
 * 회원 배송지 조회관련 api rest controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-addresses")
public class QueryMemberAddressController {

    private final QueryMemberAddressService queryMemberAddressService;

    /**
     * 회원아이디를 통해 배송지를 조회합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 배송지 목록
     * @author 최예린
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_USER'})")
    @GetMapping
    public ResponseDto<List<MemberAddressResponseDto>> getMemberAddressByMemberId(@LoginId(required = true) String loginId) {
        List<MemberAddressResponseDto> response = queryMemberAddressService.getByLoginId(loginId);

        return ResponseDto.<List<MemberAddressResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
