package shop.yesaladin.shop.category.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.QCategory;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;

/**
 * QueryDsl 을 사용하여 카테고리 관련 데이터를 조회시 사용
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Repository
public class QueryDslCategoryRepositoryImpl implements QueryCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        QCategory category = QCategory.category;
        List<Category> categories = queryFactory.selectFrom(category)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(category.count()).from(category).fetchFirst();
        return new PageImpl<>(categories, pageable, count);
    }

    @Override
    public Optional<Category> findById(Long id) {
        QCategory category = QCategory.category;
        Optional<Category> categoryOptional = Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.id.eq(id))
                .fetchFirst());

        if (categoryOptional.isEmpty()) {
            return Optional.empty();
        }

        return categoryOptional;
    }

    @Override
    public Optional<Category> findByName(String name) {
        QCategory category = QCategory.category;
        Optional<Category> categoryOptional = Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.name.eq(name))
                .fetchFirst());

        if (categoryOptional.isEmpty()) {
            return Optional.empty();
        }

        return categoryOptional;
    }

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

    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        QCategory category = QCategory.category;
        QCategory parent = new QCategory("parent");

        List<Category> categories = queryFactory.select(category)
                .from(category)
                .innerJoin(category.parent, parent)
                .fetchJoin()
                .where(category.parent.id.eq(parentId))
                .groupBy(category.id)
                .fetch();
        System.out.println("categories = " + categories);
        return null;
    }
}
