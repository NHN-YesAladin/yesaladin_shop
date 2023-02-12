package shop.yesaladin.shop.order.service.inter;

import java.time.LocalDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.NonMemberRequestDto;
import shop.yesaladin.shop.order.dto.OrderStatusChangeLogResponseDto;

/**
 * 주문 상태 변경 내역 생성과 관련한 service interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderStatusChangeLogService {

    /**
     * 회원의 주문 상태 변경 내역을 생성합니다.
     *
     * @param orderId     주문 pk
     * @param loginId     회원의 아이디
     * @param orderStatus 주문 상태
     * @return 생성된 주문 상태 변경 내역
     * @author 최예린
     * @since 1.0
     */
    OrderStatusChangeLogResponseDto createMemberOrderStatusChangeLog(
            long orderId,
            String loginId,
            OrderStatusCode orderStatus
    );

    /**
     * 비회원의 주문 상태 변경 내역을 생성합니다.
     *
     * @param orderId     주문 pk
     * @param request     비회원 정보
     * @param orderStatus 주문 상태
     * @return 생성된 주문 상태 변경 내역
     * @author 최예린
     * @since 1.0
     */
    OrderStatusChangeLogResponseDto createNonMemberOrderStatusChangeLog(
            Long orderId,
            NonMemberRequestDto request,
            OrderStatusCode orderStatus
    );

    /**
     * 주문 상태 변경 로그를 추가합니다.
     *
     * @param orderChangeDateTime 변경 이력 시간
     * @param order               상태 변경을 할 주문
     * @param code                주문 상태
     */
    void appendOrderStatusChangeLog(
            LocalDateTime orderChangeDateTime,
            Order order,
            OrderStatusCode code
    );
}
