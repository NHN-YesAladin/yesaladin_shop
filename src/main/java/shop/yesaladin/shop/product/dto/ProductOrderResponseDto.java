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
public class ProductOrderResponseDto {

    private Long productId;
    private String isbn;
    private String title;
    private Long actualPrice;
    private int discountRate;
    private int expectedPoint;
    private int quantity;

    public ProductOrderResponseDto(
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

    /**
     * 주문상품의 수량을 저장합니다.
     *
     * @param request 상품 정보
     * @author 최예린
     * @since 1.0
     */
    public void setQuantity(List<ProductOrderRequestDto> request) {
        this.quantity = request.stream()
                .filter(x -> Objects.equals(this.isbn, x.getIsbn()))
                .map(ProductOrderRequestDto::getQuantity)
                .findFirst()
                .orElse(0);
    }
}
