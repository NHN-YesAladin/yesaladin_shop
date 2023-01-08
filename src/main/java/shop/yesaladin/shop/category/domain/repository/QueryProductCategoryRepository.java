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

    Page<ProductCategory> findAll(Pageable pageable);

    Optional<ProductCategory> findByPk(Pk pk);
}
