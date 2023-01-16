package shop.yesaladin.shop.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 카테고리를 조회할때 응답으로 전달하는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseDto {

    private Long productId;
    private String ISBN;
    private String title;

    private CategoryResponseDto categoryResponseDto;

    /**
     * 입력받은 상품, 카테고리 엔티티를 통해 ProductCategoryResponseDto를 반환
     *
     * @param product
     * @param category
     * @return ProductCategoryResponseDto 상품과 카테고리의 일부 데이터
     */
    public static ProductCategoryResponseDto fromEntity(Product product, Category category) {
        return ProductCategoryResponseDto.builder()
                .categoryResponseDto(CategoryResponseDto.fromEntity(category))
                .productId(product.getId())
                .ISBN(product.getISBN())
                .title(product.getTitle()).build();
    }
}
