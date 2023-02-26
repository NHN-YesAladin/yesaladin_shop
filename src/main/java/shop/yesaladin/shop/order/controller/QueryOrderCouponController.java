package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderCouponService;

/**
 * 주문에 사용한 쿠폰 조회 관련 controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
public class QueryOrderCouponController {

    private final QueryOrderCouponService queryOrderCouponService;

    /**
     * 상품에 쿠폰을 적용합니다.
     *
     * @param loginId 회원의 아이디
     * @param request 상품 및 쿠폰 정보
     * @return 상품에 쿠폰을 적용한 정보
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/v1/order-coupons")
    public CouponOrderSheetResponseDto calculateCoupon(
            @LoginId(required = true) String loginId,
            @ModelAttribute CouponOrderSheetRequestDto request
    ) {
        return queryOrderCouponService.calculateCoupons(
                loginId,
                request
        );
    }
}
