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
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

/**
 * 쿠폰 지급 요청 관련 api를 정의하는 controller 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-coupons")
public class GiveMemberCouponController {

    private final GiveCouponService giveCouponService;

    /**
     * 쿠폰 지급 요청 메시지를 발행합니다.
     *
     * @param memberId   로그인한 회원의 로그인 아이디
     * @param requestDto 쿠폰 지급 요청 정보
     * @return 성공 여부를 담은 ResponseDto
     */
    @PostMapping
    public ResponseDto<RequestIdOnlyDto> sendCouponGiveRequest(
            @LoginId(required = true) String memberId, @RequestBody CouponGiveRequestDto requestDto
    ) {
        RequestIdOnlyDto response = giveCouponService.sendCouponGiveRequest(
                memberId,
                requestDto.getTriggerTypeCode(),
                requestDto.getCouponId(),
                requestDto.getRequestDateTime()
        );

        return ResponseDto.<RequestIdOnlyDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
