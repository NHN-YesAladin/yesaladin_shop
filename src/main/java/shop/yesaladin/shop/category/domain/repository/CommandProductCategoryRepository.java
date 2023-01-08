package shop.yesaladin.shop.category.domain.repository;

import shop.yesaladin.shop.category.domain.model.ProductCategory;

/**
 * 상품카테고리 CUD용 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface CommandProductCategoryRepository {

    ProductCategory save(ProductCategory productCategory);

}
