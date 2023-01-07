package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;

/**
 * 주문에 사용한 쿠폰 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderUsedCouponRepository {

    /**
     * 주문에 사용한 쿠폰 데이터를 등록합니다.
     *
     * @param orderUsedCoupon 주문에 사용한 쿠폰 데이터
     * @return 등록된 주문에 사용한 쿠폰 데이터
     * @author 최예린
     * @since 1.0
     */
    OrderUsedCoupon save(OrderUsedCoupon orderUsedCoupon);

}
