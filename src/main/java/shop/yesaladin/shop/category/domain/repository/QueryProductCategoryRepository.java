package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품카테고리 조회용(R) 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryProductCategoryRepository {

    /**
     * 상품 카테고리 조회
     *
     * @param pk 상품 id, 카테고리 id
     * @return Optional 처리된 ProductCategory
     */
    Optional<ProductCategory> findByPk(Pk pk);

    /**
     * 특정 상품을 통한 카테고리 전체 조회
     *
     * @param product 찾고자하는 기준이 되는 상품
     * @return 카테고리 정보의 리스트
     */
    List<CategoryResponseDto> findCategoriesByProduct(Product product);
}
