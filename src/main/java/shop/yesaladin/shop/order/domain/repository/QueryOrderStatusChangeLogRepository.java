package shop.yesaladin.shop.order.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog.Pk;

/**
 * 주문상태 변경내역 조회 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @author 배수한
 * @since 1.0
 */
public interface QueryOrderStatusChangeLogRepository {

    /**
     * pk를 통해 주문상태 변경내역 데이터를 조회합니다.
     *
     * @param pk pk
     * @return 조회된 주문상태 변경내역 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<OrderStatusChangeLog> findById(Pk pk);

    /**
     * 주문 id를 통해 주문상태 변경내역 데이터를 조회합니다.
     *
     * @param orderId 주문 id
     * @return 주문상태 변경내역 데이터
     * @author 배수한
     * @since 1.0
     */
    List<OrderStatusChangeLog> findAllByOrder_IdOrderByOrderStatusCodeDesc(Long orderId);


}
