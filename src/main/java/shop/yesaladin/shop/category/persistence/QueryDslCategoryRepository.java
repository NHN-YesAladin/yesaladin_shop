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
     * 카테고리 paging list 조회
     *
     * @param pageable size 와 page 를 가진 객체
     * @return paging 되어있는 Category Page 객체
     */
    @Override
    public Page<Category> findCategoriesByParentId(Pageable pageable, Long parentId) {
        QCategory category = QCategory.category;
        List<Category> categories = queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(category.parent.id.eq(parentId))
                .orderBy(category.order.asc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory.select(category.count())
                .where(category.parent.id.eq(parentId))
                .from(category);

        return PageableExecutionUtils.getPage(categories, pageable, countQuery::fetchFirst);
    }


    /**
     * 카테고리 이름을 통한 카테고리 조회
     *
     * @param name 카테고리 이름
     * @return Optional 처리가 된 카테고리 객체
     */
    @Override
    public Optional<Category> findByName(String name) {
        QCategory category = QCategory.category;
        return Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.name.eq(name))
                .fetchFirst());
    }

    /**
     * 카테고리 id의 마지막 값을 depth와 부모 id를 통해 조회 2차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth    2차 카테고리의 깊이 값인 1이 입력됨
     * @param parentId 2차 카테고리가 가지고있는 부모 id
     * @return Long id 만 가지고있음
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
     * 카테고리의 id의 마지막 값을 depth를 통해 조회 1차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 1차 카테고리의 깊이 값인 0이 입력됨
     * @return Long id 만 가지고있음
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
     * @param parentId 찾고자하는 카테고리의 parentId
     * @param depth    찾고자하는 카테고리의 깊이
     * @return Category 엔티티
     */
    @Override
    public List<Category> findCategories(Long parentId, Integer depth) {
        QCategory category = QCategory.category;
        return queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(parentIdEq(category, parentId), depthEq(category, depth))
                .orderBy(category.order.asc().nullsLast())
                .fetch();
    }

    /**
     * 동적 쿼리를 위한 메서드
     *  1차 카테고리의 id가 null이 아니면 where절에서 적용
     *
     * @param category Q객체
     * @param parentId 찾고자하는 1차 카테고리의 id , nullable
     * @return
     */
    private BooleanExpression parentIdEq(QCategory category, Long parentId) {
        if (Objects.isNull(parentId)) {
            return null;
        }
        return category.parent.id.eq(parentId);
    }

    /**
     * 동적 쿼리를 위한 메서드
     *  카테고리의 깊이가 null이 아니면 where절에서 적용
     *
     * @param category Q객체
     * @param depth 찾고자하는 카테고리의 깊이 , nullable
     * @return
     */
    private BooleanExpression depthEq(QCategory category, Integer depth) {

        if (Objects.isNull(depth)) {
            return null;
        }
        return category.depth.eq(depth);
    }


    /**
     * id 를 통해 카테고리를 조회 할 경우, N+1을 해결 하기 위해 fetch join 실행
     *
     * @param id 찾고자하는 카테고리 id
     * @return Optional<Category>
     */
    @Override
    public Optional<Category> findById(Long id) {
        QCategory category = QCategory.category;

        return Optional.ofNullable(queryFactory.selectFrom(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(category.id.eq(id))
                .fetchFirst());
    }


}
