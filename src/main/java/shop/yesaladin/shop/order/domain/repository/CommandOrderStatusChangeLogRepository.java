package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;

public interface CommandOrderStatusChangeLogRepository {

    OrderStatusChangeLog save(OrderStatusChangeLog orderStatusChangeLog);
}
