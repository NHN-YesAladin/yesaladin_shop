package shop.yesaladin.shop.order.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;

/**
 * 주문 조회 관련 repository 클래스입니다.
 *
 * @param <T> 주문의 상속 타입
 * @author 최예린, 김홍대
 * @since 1.0
 */
public interface QueryOrderRepository<T extends Order> {

    /**
     * pk를 통해 주문 데이터를 조회합니다.
     *
     * @param id pk
     * @return 조회된 주문 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<T> findById(Long id);

    /**
     * 기간 내의 모든 주문 데이터의 요약본을 페이지네이션하여 조회합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @param size      한 번에 조회할 데이터의 수
     * @param page      조회할 데이터의 페이지(1 base)
     * @return 페이지네이션 된 기간 내의 모든 주문 데이터의 요약
     * @author 김홍대
     * @since 1.0
     */
    List<OrderSummaryDto> findAllOrdersInPeriod(
            LocalDate startDate,
            LocalDate endDate,
            int size,
            int page
    );

    /**
     * 기간 내의 주문 데이터의 수를 반환합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @return 기간 내의 주문 데이터 개수
     * @author 김홍대
     * @since 1.0
     */
    int getCountOfOrdersInPeriod(LocalDate startDate, LocalDate endDate);
}
