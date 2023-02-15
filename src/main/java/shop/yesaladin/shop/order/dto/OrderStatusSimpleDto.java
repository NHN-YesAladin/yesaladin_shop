package shop.yesaladin.shop.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

/**
 * 현 주문의 최근 상태 코드를 알아내기 위한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@RequiredArgsConstructor
public class OrderStatusSimpleDto {

    private final Long orderId;
    private final OrderStatusCode orderStatusCode;
}
