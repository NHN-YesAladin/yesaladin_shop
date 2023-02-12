package shop.yesaladin.shop.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.coupon.dto.CouponGiveRequestDto;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-coupons")
public class GiveMemberCouponController {

    private final GiveCouponService giveCouponService;

    @PostMapping
    public ResponseDto<Void> sendCouponGiveRequest(
            @LoginId(required = true) String memberId, @RequestBody CouponGiveRequestDto requestDto
    ) {
        giveCouponService.sendCouponGiveRequest(
                memberId,
                requestDto.getTriggerTypeCode(),
                requestDto.getCouponId()
        );

        return ResponseDto.<Void>builder().success(true).status(HttpStatus.OK).data(null).build();
    }
}
