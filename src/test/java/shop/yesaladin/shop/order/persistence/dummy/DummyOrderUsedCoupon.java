package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.order.domain.dummy.CouponIssuance;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;

public class DummyOrderUsedCoupon {

    public static OrderUsedCoupon orderUsedCoupon(
            MemberOrder memberOrder,
            CouponIssuance couponIssuance
    ) {
        return OrderUsedCoupon.create(memberOrder, couponIssuance);
    }
}
