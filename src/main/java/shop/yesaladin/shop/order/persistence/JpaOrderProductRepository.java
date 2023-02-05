package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.repository.CommandOrderProductRepository;

/**
 * 주문 상품 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaOrderProductRepository extends Repository<OrderProduct, Long>,
        CommandOrderProductRepository {

}
