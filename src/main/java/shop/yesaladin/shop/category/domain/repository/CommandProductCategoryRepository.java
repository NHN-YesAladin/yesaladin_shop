package shop.yesaladin.shop.category.domain.repository;

import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;

/**
 * 상품카테고리 CUD용 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface CommandProductCategoryRepository {

    /**
     * 상품 카테고리 생성 및 수정
     *
     * @param productCategory
     * @return 등록된 상품 카테고리 객체
     */
    ProductCategory save(ProductCategory productCategory);

    /**
     * 상품 id 와 카테고리 id를 통한 상품 카테고리 삭제
     *
     * @param pk 상품 id, 카테고리 id
     */
    void deleteByPk(Pk pk);

}
