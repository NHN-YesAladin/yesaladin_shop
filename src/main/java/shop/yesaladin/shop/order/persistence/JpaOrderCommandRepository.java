package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRepository;

/**
 * 주문 엔티티를 위한(조회 제외) Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @author 김홍대
 * @since 1.0
 */
public interface JpaOrderCommandRepository<T extends Order> extends Repository<T, Long>,
        CommandOrderRepository<T> {

}
