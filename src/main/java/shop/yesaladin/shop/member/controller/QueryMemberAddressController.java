package shop.yesaladin.shop.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

/**
 * 회원 배송지 조회관련 api rest controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members/{loginId}/addresses")
public class QueryMemberAddressController {

    private final QueryMemberAddressService queryMemberAddressService;

    /**
     * 회원아이디를 통해 배송지를 조회합니다.
     *
     * @param loginId 회원 아이디
     * @return 회원의 배송지 목록
     * @author 최예린
     * @since 1.0
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberAddressQueryDto> getMemberAddressByMemberId(@PathVariable String loginId) {
        return queryMemberAddressService.findByLoginId(loginId);
    }
}
