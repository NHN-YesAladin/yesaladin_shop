package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

/**
 * 주문 조회시 응답으로 반환할 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@RequiredArgsConstructor
public class OrderSummaryResponseDto {

    private final Long orderId;
    private final String orderNumber;
    private final LocalDateTime orderDateTime;
    private final String orderName;
    private final Long orderAmount;
    private final OrderStatusCode orderStatusCode;
    private final Long memberId;
    private final String MemberName;

}
