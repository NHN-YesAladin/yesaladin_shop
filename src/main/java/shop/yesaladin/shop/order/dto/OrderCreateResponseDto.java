package shop.yesaladin.shop.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.Order;

/**
 * 주문 생성시 반환하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCreateResponseDto {

    private Long orderId;
    private String orderNumber;
    private String orderName;
    private Long totalAmount;
    private Integer shippingFee;
    private Integer wrappingFee;

    /**
     * 주문 엔티티를 orderCreateResponseDto 로 변환합니다.
     *
     * @param order 주문 엔티티
     * @return orderCreateResponseDto
     * @author 최예린
     * @since 1.0
     */
    public static OrderCreateResponseDto fromEntity(Order order) {
        return new OrderCreateResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getName(),
                order.getTotalAmount(),
                order.getShippingFee(),
                order.getWrappingFee()
        );
    }
}
