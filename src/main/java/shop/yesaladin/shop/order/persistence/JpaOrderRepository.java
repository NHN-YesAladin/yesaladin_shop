package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;

/**
 * 주문 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaOrderRepository<T extends Order> extends Repository<T, Long>,
        CommandOrderRepository<T>,
        QueryOrderRepository<T> {

}
