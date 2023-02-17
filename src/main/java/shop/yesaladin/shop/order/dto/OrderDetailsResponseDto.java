package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.payment.dto.PaymentResponseDto;
import shop.yesaladin.shop.product.dto.ProductOrderQueryResponseDto;

/**
 * 주문 상세 조회시 사용하는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailsResponseDto {

    //주문 기본 정보
    private OrderResponseDto order;

    //상품 정보
    private List<OrderProductResponseDto> orderProducts;

    //가격 정보
    private long productsAmount;
    private long discountsAmount;

    //결제 정보
    private PaymentResponseDto payment;

    public void setOrder(OrderResponseDto order) {
        this.order = order;
    }

    public void setOrderProducts(List<OrderProductResponseDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void setPayment(PaymentResponseDto payment) {
        this.payment = payment;
    }

    /**
     * 주문 상품들의 총합 계산
     */
    public void calculateAmounts() {
        this.productsAmount = this.orderProducts.stream().map(p -> {
            ProductOrderQueryResponseDto productDto = p.getProductDto();
            return (productDto.getActualPrice() * p.getQuantity());
        }).mapToLong(Long::longValue).sum();
        calculateDiscountsAmount();
    }

    /**
     * 주문 상품을 구매시 할인 받은 금액의 총합 계산
     */
    public void calculateDiscountsAmount() {
        // 할인금액(쿠폰, 전체 할인) = 물건들 실제 값 - 총 가격(배송/포장비 제외) - 사용 포인트
        this.discountsAmount =
                this.productsAmount - (this.order.getTotalAmount() + this.order.getUsedPoint());
    }
}
