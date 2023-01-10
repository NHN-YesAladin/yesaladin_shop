package shop.yesaladin.shop.order.domain.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;

/**
 * 주문 조회 관련 repository 클래스입니다.
 *
 * @author 최예린, 김홍대
 * @since 1.0
 */
public interface QueryOrderRepository {

    /**
     * pk를 통해 주문 데이터를 조회합니다.
     *
     * @param id pk
     * @return 조회된 주문 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<Order> findById(Long id);

    /**
     * 기간 내의 모든 주문 데이터의 요약본을 페이지네이션하여 조회합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @param pageable  페이지네이션 정보
     * @return 페이지네이션 된 기간 내의 모든 주문 데이터의 요약
     * @author 김홍대
     * @since 1.0
     */
    Page<OrderSummaryDto> findAllOrdersInPeriod(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
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
    long getCountOfOrdersInPeriod(LocalDate startDate, LocalDate endDate);
}
