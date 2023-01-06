package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.Order;

public interface CommandOrderRepository<T extends Order> {

    T save(T order);
}
