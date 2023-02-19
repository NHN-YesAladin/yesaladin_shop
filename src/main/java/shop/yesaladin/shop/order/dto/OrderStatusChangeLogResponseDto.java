package shop.yesaladin.shop.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

import java.time.LocalDateTime;

/**
 * 주문 상태 변경 내역 생성 후 반환하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStatusChangeLogResponseDto {

    private String orderNumber;
    private String name;
    private LocalDateTime changeDateTime;
    private OrderStatusCode orderStatusCode;

    /**
     * 주문 상태 변경 내역을 dto 클래스로 변환합니다.
     *
     * @param orderStatusChangeLog 주문 상태 변경 내역
     * @return 주문 상태 변경 내역 dto 클래스
     * @author 최예린
     * @since 1.0
     */
    public static OrderStatusChangeLogResponseDto fromEntity(OrderStatusChangeLog orderStatusChangeLog) {
        return new OrderStatusChangeLogResponseDto(
                orderStatusChangeLog.getOrder().getOrderNumber(),
                orderStatusChangeLog.getOrder().getName(),
                orderStatusChangeLog.getPk().getChangeDateTime(),
                orderStatusChangeLog.getOrderStatusCode()
        );
    }
}
