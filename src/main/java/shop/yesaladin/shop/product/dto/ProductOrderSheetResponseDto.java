package shop.yesaladin.shop.product.dto;

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
public class ProductOrderSheetResponseDto {

    private Long productId;
    private String isbn;
    private String title;
    private Long actualPrice;
    private int discountRate;
    private Long expectedPoint;
    private int quantity;

    public ProductOrderSheetResponseDto(
            Long productId,
            String isbn,
            String title,
            Long actualPrice,
            int discountRate,
            Long expectedPoint
    ) {
        this.productId = productId;
        this.isbn = isbn;
        this.title = title;
        this.actualPrice = actualPrice;
        this.discountRate = discountRate;
        this.expectedPoint = expectedPoint;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}