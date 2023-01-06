package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.Order;

/**
 * 주문 조회 관련 repository 클래스입니다.
 *
 * @param <T> 주문의 상속 타입
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderRepository<T extends Order> {

    /**
     * pk를 통해 주문 데이터를 조회합니다.
     *
     * @param id pk
     * @return 조회된 주문 데이터
     *
     * @author 최예린
     * @since 1.0
     */
    Optional<T> findById(Long id);
}
