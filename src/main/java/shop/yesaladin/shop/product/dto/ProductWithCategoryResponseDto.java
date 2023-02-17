package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

/**
 * 상품과 카테고리 리스트를 반환하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@ToString
@Getter
@AllArgsConstructor
public class ProductWithCategoryResponseDto {

    private String isbn;
    private long actualPrice;
    private int discountRate;
    private boolean isSeparatelyDiscount;
    private int givenPointRate;
    private boolean isGivenPoint;
    private TotalDiscountRate totalDiscountRate;
    private ProductSavingMethodCode productSavingMethodCode;
    private List<Long> categoryList;
}
