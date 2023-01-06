package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog.Pk;
import shop.yesaladin.shop.order.domain.repository.CommandOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderStatusChangeLogRepository;

/**
 * 주문상태 변경내역 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaOrderStatusChangeLogRepository extends Repository<OrderStatusChangeLog, Pk>,
        CommandOrderStatusChangeLogRepository, QueryOrderStatusChangeLogRepository {

}
