package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;

public class DummyOrderUsedCoupon {

    public static OrderUsedCoupon orderUsedCoupon(
            MemberOrder memberOrder, MemberCoupon memberCoupon
    ) {
        return OrderUsedCoupon.create(memberOrder, memberCoupon);
    }
}
