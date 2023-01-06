package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.order.domain.dummy.CouponIssuance;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon.Pk;

public class DummyOrderUsedCoupon {

    public static OrderUsedCoupon orderUsedCoupon(
            Long orderId,
            Long couponIssuanceId,
            Order order,
            CouponIssuance couponIssuance
    ) {
        return OrderUsedCoupon.builder()
                .pk(new Pk(orderId, couponIssuanceId))
                .order(order)
                .couponIssuance(couponIssuance).build();
    }

}
