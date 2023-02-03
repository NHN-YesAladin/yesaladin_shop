package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.OrderProduct;

/**
 * 주문 상품의 조회 관련 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderProductRepository {

    Optional<OrderProduct> findById(long id);
}
