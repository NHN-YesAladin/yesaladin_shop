package shop.yesaladin.shop.product.dto;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문한 상품의 정보를 반환하기 위한 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class OrderProductResponseDto {

    private Long productId;
    private String isbn;
    private String title;
    private Long actualPrice;
    private int discountRate;
    private int expectedPoint;
    private int quantity;

    public OrderProductResponseDto(
            Long productId,
            String isbn,
            String title,
            Long actualPrice,
            int discountRate,
            int expectedPoint
    ) {
        this.productId = productId;
        this.isbn = isbn;
        this.title = title;
        this.actualPrice = actualPrice;
        this.discountRate = discountRate;
        this.expectedPoint = expectedPoint;
    }

    public void setQuantity(List<OrderProductRequestDto> request) {
        this.quantity = request.stream()
                .filter(x -> Objects.equals(this.isbn, x.getIsbn()))
                .map(OrderProductRequestDto::getQuantity)
                .findFirst()
                .orElse(0);
    }
}
