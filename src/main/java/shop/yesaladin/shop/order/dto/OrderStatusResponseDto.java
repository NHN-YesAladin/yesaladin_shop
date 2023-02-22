package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.OrderCode;

/**
 * @author 배수한
 * @since 1.0
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponseDto {

    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDateTime;
    private String orderName;
    private Long totalAmount;
    private String loginId;
    private String receiverName;
    private OrderCode orderCode;

}
