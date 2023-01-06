package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.Order;

/**
 * 주문 등록 및 수정 관련 repository 클래스입니다.
 *
 * @param <T> 주문의 상속 타입
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderRepository<T extends Order> {

    /**
     * 주문 데이터를 등록합니다.
     *
     * @param order 주문 데이터
     * @return 등록된 주문 데이터
     *
     * @author 최예린
     * @since 1.0
     */
    T save(T order);
}
