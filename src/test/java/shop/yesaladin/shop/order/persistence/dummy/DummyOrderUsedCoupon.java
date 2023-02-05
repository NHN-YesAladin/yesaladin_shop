package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCoupon;

public class DummyOrderUsedCoupon {

    public static OrderCoupon orderUsedCoupon(
            MemberOrder memberOrder, MemberCoupon memberCoupon
    ) {
        return OrderCoupon.create(memberOrder, memberCoupon);
    }
}
