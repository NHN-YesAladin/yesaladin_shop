package shop.yesaladin.shop.product.dto;

import java.util.List;
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
    private long actualPrice;
    private int discountRate;
    private Boolean isGivenPoint;
    private int givenPointRate;
    private long quantity;
    private List<String> categories;

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
