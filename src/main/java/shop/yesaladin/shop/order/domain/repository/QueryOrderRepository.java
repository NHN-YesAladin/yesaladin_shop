package shop.yesaladin.shop.order.domain.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;

/**
 * 주문 조회 관련 repository 클래스입니다.
 *
 * @author 김홍대
 * @author 배수한
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
     * 특정 회원의 기간 내 모든 주문 데이터의 요약본을 페이지네이션하여 조회합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @param memberId  주문 내역을 조회할 회원의 아이디
     * @param pageable  페이지네이션 정보
     * @return 페이지네이션 된 기간 내의 모든 주문 데이터의 요약
     * @author 김홍대
     * @since 1.0
     */
    Page<OrderSummaryDto> findAllOrdersInPeriodByMemberId(
            LocalDate startDate,
            LocalDate endDate,
            long memberId,
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

    /**
     * 특정 회원의 기간 내 주문 데이터의 수를 반환합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @return 기간 내의 주문 데이터 개수
     * @author 김홍대
     * @since 1.0
     */
    long getCountOfOrdersInPeriodByMemberId(LocalDate startDate, LocalDate endDate, long memberId);

    /**
     * unique key인 주문 번호를 통해 주문 데이터를 조회합니다.
     *
     * @param orderNumber 주문번호
     * @return 조회된 주문 데이터
     * @author 배수한
     * @since 1.0
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 특정 회원의 기간 내 모든 주문 데이터의 요약본을 페이지네이션하여 조회합니다.
     *
     * @param startDate 조회 기간 시작 일자
     * @param endDate   조회 기간 마지막 일자
     * @param memberId  주문 내역을 조회할 회원의 아이디
     * @param pageable  페이지네이션 정보
     * @return 페이지네이션 된 기간 내의 모든 주문 데이터의 요약
     * @author 배수한
     * @since 1.0
     */
    Page<OrderSummaryResponseDto> findOrdersInPeriodByMemberId(
            LocalDate startDate,
            LocalDate endDate,
            long memberId,
            Pageable pageable
    );

    /**
     * 결제 이후 정보 중 주문 pk를 통해서 주문자 이름과 주소를 조회합니다.
     *
     * @param orderId 주문 pk
     * @return 주문자 이름, 주문 주소지
     * @author 배수한
     * @since 1.0
     */
    Optional<OrderPaymentResponseDto> findPaymentDtoByMemberOrderId(long orderId);

    /**
     * OrderStatusCode에 따라 회원 주문을 조회합니다.
     *
     * @param loginId 회원의 로그인 아이디
     * @param code 주문 상태
     * @param pageable 페이징 처리 요소
     * @return 페이징 처리된 주문 정보
     * @author 배수한
     * @since 1.0
     */
    Page<OrderStatusResponseDto> findSuccessStatusResponsesByLoginIdAndStatus(
            String loginId,
            OrderStatusCode code,
            Pageable pageable
    );

    long getOrderCountByStatusCode(String loginId, OrderStatusCode code);
}
