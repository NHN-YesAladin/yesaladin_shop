package shop.yesaladin.shop.category.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.domain.model.querydsl.QCategory;
import shop.yesaladin.shop.category.domain.model.querydsl.QProductCategory;
import shop.yesaladin.shop.category.domain.repository.QueryProductCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 카테고리 조회용 querydsl 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Repository
public class QueryDslProductCategoryRepository implements QueryProductCategoryRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<ProductCategory> findByPk(Pk pk) {
        QProductCategory productCategory = QProductCategory.productCategory;
        return Optional.ofNullable(queryFactory.selectFrom(productCategory)
                .where(productCategory.pk.eq(pk))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<CategoryResponseDto> findCategoriesByProduct(Product product) {
        QProductCategory productCategory = QProductCategory.productCategory;
        QCategory category = QCategory.category;
        return queryFactory.select(Projections.constructor(CategoryResponseDto.class,
                        productCategory.category.id,
                        productCategory.category.name,
                        productCategory.category.isShown,
                        productCategory.category.order,
                        productCategory.category.parent.id,
                        productCategory.category.parent.name))
                .from(productCategory)
                .innerJoin(productCategory.category, category)
                .where(productCategory.product.eq(product))
                .fetch();
    }
}
