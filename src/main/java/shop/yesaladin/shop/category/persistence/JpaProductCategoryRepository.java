package shop.yesaladin.shop.category.persistence;

import java.util.List;
import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.repository.CommandProductCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryProductCategoryRepository;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 카테고리 관계 테이블에 JPA로 접근 가능한 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface JpaProductCategoryRepository extends
        Repository<ProductCategory, ProductCategory.Pk>, CommandProductCategoryRepository,
        QueryProductCategoryRepository {

}
