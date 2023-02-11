package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

public class DummyOrderStatusChangeLog {

    public static OrderStatusChangeLog orderStatusChangeLog(Order order) {
        return OrderStatusChangeLog.create(order, LocalDateTime.now(), OrderStatusCode.ORDER);
    }

    public static OrderStatusChangeLog orderStatusChangeLog(Order order, OrderStatusCode code) {
        return OrderStatusChangeLog.create(order, LocalDateTime.now(), code);
    }


}
