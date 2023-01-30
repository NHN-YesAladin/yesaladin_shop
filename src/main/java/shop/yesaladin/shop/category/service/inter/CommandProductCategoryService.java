package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.dto.ProductCategoryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * @author 배수한
 * @since 1.0
 */
public interface CommandProductCategoryService {

    /**
     * 상품 카테고리 엔티티를 저장하기 위한 메서드
     *
     * @param productCategory 상품 카테고리 엔티티
     * @return 상품 및 카테고리의 일부정보를 담은 dto
     */
    ProductCategoryResponseDto register(ProductCategory productCategory);

    /**
     * 상품을 통한 상품 카테고리 삭제 메서드
     *
     * @param product
     */
    void deleteByProduct(Product product);
}
