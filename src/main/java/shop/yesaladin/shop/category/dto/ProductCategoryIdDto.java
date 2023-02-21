package shop.yesaladin.shop.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품과 카테고리의 id를 보유하고있는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryIdDto {

    private Long productId;
    private Long categoryId;

    /**
     * 상품, 카테고리 엔티티에서 id를 추출하여 ProductCategoryIdDto를 반환
     *
     * @param product  상품 엔티티
     * @param category 카테고리 엔티티
     * @return 상품 카테고리 엔티티의 id만 가지고있는 dto
     */
    public static ProductCategoryIdDto fromEntity(Product product, Category category) {
        return ProductCategoryIdDto.builder()
                .categoryId(category.getId())
                .productId(product.getId())
                .build();
    }

}
