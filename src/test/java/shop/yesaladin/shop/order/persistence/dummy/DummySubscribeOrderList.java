package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;

public class DummySubscribeOrderList {

    public static SubscribeOrderList subscribeOrderList(
            Subscribe subscribe, MemberOrder memberOrder
    ) {
        return SubscribeOrderList.builder()
                .isTransported(false)
                .subscribe(subscribe)
                .memberOrder(memberOrder)
                .build();
    }
}
