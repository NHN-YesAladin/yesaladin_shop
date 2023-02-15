package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

/**
 * 주문 조회시 응답으로 반환할 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponseDto {

    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDateTime;
    private String orderName;
    private Long orderAmount;
    private OrderStatusCode orderStatusCode;
    private Long memberId;
    private String memberName;
    private Long orderProductCount;
    private Integer productTotalCount;
    private OrderCode orderCode;

    public void setOrderStatusCode(OrderStatusCode orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }
}
