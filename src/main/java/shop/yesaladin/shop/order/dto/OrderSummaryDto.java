package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;

/**
 * 주문 데이터에 대한 대략적인 정보를 가지는 응답용 DTO
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSummaryDto {

    private final String orderNumber;
    private final LocalDateTime orderDateTime;
    private final OrderCode orderCode;

    public static OrderSummaryDto fromEntity(Order order) {
        return new OrderSummaryDto(
                order.getOrderNumber(),
                order.getOrderDateTime(),
                order.getOrderCode()
        );
    }
}
