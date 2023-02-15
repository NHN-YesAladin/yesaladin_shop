package shop.yesaladin.shop.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.coupon.dto.CouponUseRequestDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;

/**
 * 쿠폰 사용 관련 요청을 처리하는 controller 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-coupons/usage")
public class UseMemberCouponController {

    private final UseCouponService useCouponService;

    /**
     * 회원 아이디와 쿠폰 코드를 통해 쿠폰 사용을 요청합니다.
     *
     * @param memberId 회원 아이디
     * @param dto      사용할 쿠폰 코드 리스트가 담긴 dto
     * @return         쿠폰 사용 요청 메시지에 대한 응답
     */
    @PostMapping
    public ResponseDto<RequestIdOnlyDto> sendCouponUseRequest(
            @LoginId(required = true) String memberId, @RequestBody CouponUseRequestDto dto
    ) {
        RequestIdOnlyDto requestIdOnlyDto = useCouponService.sendCouponUseRequest(memberId,
                dto.getCouponCodes()
        );

        return ResponseDto.<RequestIdOnlyDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(requestIdOnlyDto)
                .build();
    }
}
