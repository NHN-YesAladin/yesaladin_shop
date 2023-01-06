package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog.Pk;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

public class DummyOrderStatusChangeLog {
    public static OrderStatusChangeLog orderStatusChangeLog(Long orderId, Order order) {
        return OrderStatusChangeLog.builder()
                .pk(new Pk(orderId, LocalDateTime.now()))
                .orderStatusCode(OrderStatusCode.ORDER)
                .order(order)
                .build();
    }

}
