package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문한 상품의 정보를 반환하기 위한 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderSheetResponseDto {

    private Long productId;
    private String isbn;
    private String title;
    private long actualPrice;
    private int discountRate;
    private Boolean isGivenPoint;
    private int givenPointRate;
    private long quantity;
    private List<String> categories;

    public ProductOrderSheetResponseDto(
            Long productId,
            String isbn,
            String title,
            long actualPrice,
            int discountRate,
            Boolean isGivenPoint,
            int givenPointRate,
            long quantity
    ) {
        this.productId = productId;
        this.isbn = isbn;
        this.title = title;
        this.actualPrice = actualPrice;
        this.discountRate = discountRate;
        this.isGivenPoint = isGivenPoint;
        this.givenPointRate = givenPointRate;
        this.quantity = quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
