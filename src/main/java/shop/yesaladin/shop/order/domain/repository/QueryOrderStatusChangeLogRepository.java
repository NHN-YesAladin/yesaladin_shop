package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog.Pk;

public interface QueryOrderStatusChangeLogRepository {

    Optional<OrderStatusChangeLog> findById(Pk pk);
}
