package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;

public interface CommandOrderUsedCouponRepository {

    OrderUsedCoupon save(OrderUsedCoupon orderUsedCoupon);

}
