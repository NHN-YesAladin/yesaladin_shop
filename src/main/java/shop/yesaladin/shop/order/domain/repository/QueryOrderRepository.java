package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.Order;

public interface QueryOrderRepository<T extends Order> {

    Optional<T> findById(Long id);
}
