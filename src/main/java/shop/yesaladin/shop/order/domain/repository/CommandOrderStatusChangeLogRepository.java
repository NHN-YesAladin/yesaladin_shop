package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;

/**
 * 주문상태 변경내역 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderStatusChangeLogRepository {

    /**
     * 주문상태 변경내역 데이터를 등록합니다.
     *
     * @param orderStatusChangeLog 주문상태 변경내역 데이터
     * @return 등록된 주문상태 변경내역 데이터
     *
     * @author 최예린
     * @since 1.0
     */
    OrderStatusChangeLog save(OrderStatusChangeLog orderStatusChangeLog);
}
