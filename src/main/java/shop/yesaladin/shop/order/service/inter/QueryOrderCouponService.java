package shop.yesaladin.shop.order.service.inter;

import shop.yesaladin.shop.coupon.dto.CouponOrderSheetRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetResponseDto;

/**
 * 주문에 사용한 쿠폰 조회 관련 service interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderCouponService {

    /**
     * 주문 상품에 쿠폰을 적용합니다.
     *
     * @param loginId 회원의 아이디
     * @param request 상품과 쿠폰
     * @return 상품의 총 할인가
     * @author 최예린
     * @since 1.0
     */
    CouponOrderSheetResponseDto calculateCoupons(
            String loginId,
            CouponOrderSheetRequestDto request
    );
}
