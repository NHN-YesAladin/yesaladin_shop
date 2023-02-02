package shop.yesaladin.shop.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class CommandMemberCouponController {

    private final CommandMemberCouponService commandMemberCouponService;

    @PostMapping
    public void createMemberCoupon(@RequestBody List<MemberCouponRequestDto> requestDtos) {
        commandMemberCouponService.createMemberCoupons(requestDtos);
    }
}
