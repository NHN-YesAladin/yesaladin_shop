package shop.yesaladin.shop.category.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.querydsl.QCategory;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;

/**
 * QueryDsl 을 사용하여 카테고리 관련 데이터를 조회시 사용
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Repository
public class QueryDslCategoryRepository implements QueryCategoryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Page<Category> findCategoriesByParentId(Pageable pageable, Long parentId) {
        QCategory category = QCategory.category;
        List<Category> categories = queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(category.parent.id.eq(parentId),category.isDisable.isFalse())
                .orderBy(category.order.asc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory.select(category.count())
                .where(category.parent.id.eq(parentId),category.isDisable.isFalse())
                .from(category);

        return PageableExecutionUtils.getPage(categories, pageable, countQuery::fetchFirst);
    }


    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<Category> findByName(String name) {
        QCategory category = QCategory.category;
        return Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.name.eq(name),category.isDisable.isFalse())
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public CategoryOnlyIdDto getLatestChildIdByDepthAndParentId(int depth, Long parentId) {
        QCategory category = QCategory.category;
        Long fetchOne = queryFactory.select(category.id)
                .from(category)
                .where(category.depth.eq(depth).and(category.parent.id.eq(parentId)))
                .orderBy(category.id.desc())
                .fetchFirst();
        if (Objects.isNull(fetchOne)) {
            return new CategoryOnlyIdDto(parentId);
        }
        return new CategoryOnlyIdDto(fetchOne);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public CategoryOnlyIdDto getLatestIdByDepth(int depth) {
        QCategory category = QCategory.category;
        Long fetchOne = queryFactory.select(category.id)
                .from(category)
                .where(category.depth.eq(depth))
                .orderBy(category.id.desc())
                .fetchFirst();

        if (Objects.isNull(fetchOne)) {
            return new CategoryOnlyIdDto(0L);
        }
        return new CategoryOnlyIdDto(fetchOne);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public int getLatestChildOrderByDepthAndParentId(int depth, Long parentId) {
        QCategory category = QCategory.category;
        Integer order = queryFactory.select(category.order)
                .from(category)
                .where(category.depth.eq(depth).and(category.parent.id.eq(parentId)))
                .orderBy(category.id.desc())
                .fetchFirst();
        if (Objects.isNull(order)) {
            return 0;
        }
        return order;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public int getLatestOrderByDepth(int depth) {
        QCategory category = QCategory.category;
        Integer order = queryFactory.select(category.order)
                .from(category)
                .where(category.depth.eq(depth))
                .orderBy(category.id.desc())
                .fetchFirst();
        if (Objects.isNull(order)) {
            return 0;
        }
        return order;
    }


    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<Category> findCategories(Long parentId, Integer depth) {
        QCategory category = QCategory.category;
        return queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(parentIdEq(category, parentId), depthEq(category, depth),category.isDisable.isFalse())
                .orderBy(category.order.asc().nullsLast())
                .fetch();
    }

    /**
     * {@inheritDoc}
     *
     */
    private BooleanExpression parentIdEq(QCategory category, Long parentId) {
        if (Objects.isNull(parentId)) {
            return null;
        }
        return category.parent.id.eq(parentId);
    }

    /**
     * {@inheritDoc}
     *
     */
    private BooleanExpression depthEq(QCategory category, Integer depth) {

        if (Objects.isNull(depth)) {
            return null;
        }
        return category.depth.eq(depth);
    }


    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<Category> findById(Long id) {
        QCategory category = QCategory.category;

        return Optional.ofNullable(queryFactory.selectFrom(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(category.id.eq(id), category.isDisable.isFalse())
                .fetchFirst());
    }


}
