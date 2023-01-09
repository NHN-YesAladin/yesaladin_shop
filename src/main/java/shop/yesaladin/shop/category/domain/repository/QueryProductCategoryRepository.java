package shop.yesaladin.shop.category.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;

/**
 * 상품카테고리 조회용(R) 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryProductCategoryRepository {

    /**
     * 상품 카테고리 list 조회
     *
     * @param pageable size 와 page 를 가진 객체
     * @return paging 되어있는 ProductCategory Page 객체
     */
    Page<ProductCategory> findAll(Pageable pageable);

    /**
     * 상품 카테고리 조회
     *
     * @param pk 상품 id, 카테고리 id
     * @return Optional 처리된 ProductCategory
     */
    Optional<ProductCategory> findByPk(Pk pk);
}
