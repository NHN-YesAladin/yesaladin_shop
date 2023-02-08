package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

/**
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponseDto {

    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDateTime;
    private String orderName;
    private Long orderAmount;
    private String loginId;
    private String receiverName;
    private OrderCode orderCode;

}
