package shop.yesaladin.shop.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 주문 상품 생성 요청을 위한 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderProductCreateRequestDto {

    private int quantity;
    private Boolean isCancelled;
    private Long productId;
    private Long orderId;

    /**
     * 주문 상폼 생성 요청 dto 클래스를 엔티티로 변환합니다.
     *
     * @param product 상품
     * @param order   주문
     * @return 주문 상품 엔티티
     * @author 최예린
     * @since 1.0
     */
    public OrderProduct toEntity(Product product, Order order) {
        return OrderProduct.builder()
                .quantity(this.quantity)
                .isCancelled(this.isCancelled)
                .product(product)
                .order(order)
                .build();
    }
}
