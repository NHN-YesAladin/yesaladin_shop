package shop.yesaladin.shop.category.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.QCategory;
import shop.yesaladin.shop.category.domain.repository.QueryComplexCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;

/**
 * QueryDsl 을 사용하여 카테고리 관련 데이터를 조회시 사용
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Repository
public class QueryComplexCategoryRepositoryImpl implements QueryComplexCategoryRepository {

    private final JPAQueryFactory queryFactory;

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
}
