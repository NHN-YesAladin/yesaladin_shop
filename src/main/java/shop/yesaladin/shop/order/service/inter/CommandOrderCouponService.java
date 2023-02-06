package shop.yesaladin.shop.order.service.inter;

import java.util.List;
import shop.yesaladin.shop.order.domain.model.OrderCoupon;

/**
 * 주문에 사용한 쿠폰의 생성/수정/삭제 관련한 서비스 interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderCouponService {

    /**
     * 주문에 사용한 쿠폰을 생성합니다.
     *
     * @param orderId     주문 pk
     * @param couponCodes 쿠폰 코드 리스트
     * @return 생성된 주문에 사용한 쿠폰 리스트
     */
    List<OrderCoupon> createOrderCoupons(Long orderId, List<String> couponCodes);
}
