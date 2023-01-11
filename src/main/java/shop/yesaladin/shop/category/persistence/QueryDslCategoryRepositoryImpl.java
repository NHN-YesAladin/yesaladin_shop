package shop.yesaladin.shop.category.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.querydsl.QCategory;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategorySimpleDto;
import shop.yesaladin.shop.category.dto.querydsl.QCategorySimpleDto;

/**
 * QueryDsl 을 사용하여 카테고리 관련 데이터를 조회시 사용
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Repository
public class QueryDslCategoryRepositoryImpl implements QueryCategoryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 카테고리 paging list 조회
     *
     * @param pageable size 와 page 를 가진 객체
     * @return paging 되어있는 Category Page 객체
     */
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

    /**
     * id를 통한 카테고리 조회
     *
     * @param id 카테고리 id
     * @return Optional 처리가 된 카테고리 객체
     */
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

    /**
     * 카테고리 이름을 통한 카테고리 조회
     *
     * @param name 카테고리 이름
     * @return Optional 처리가 된 카테고리 객체
     */
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

    /**
     * 카테고리 id의 마지막 값을 depth와 부모 id를 통해 조회
     *   2차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 2차 카테고리의 깊이 값인 1이 입력됨
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
     * 카테고리의 id의 마지막 값을 depth를 통해 조회
     *   1차 카테고리의 마지막 id를 찾아오기 위해 사용
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
     * parentId 를 통해서 DtoProjection 으로 데이터를 선별하여 return 하는 기능
     *
     * @param parentId 찾고자하는 카테고리의 parentId
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */
    @Override
    public List<CategorySimpleDto> findSimpleDtosByParentId(Long parentId) {
        QCategory category = QCategory.category;
        List<CategorySimpleDto> simpleDtoList = queryFactory.select(new QCategorySimpleDto(
                        category.id,
                        category.name,
                        category.isShown,
                        category.order
                ))
                .from(category)
                .where(category.parent.id.eq(parentId))
                .fetch();
        log.info("\n getCategoriesByParentId size =  {} ", simpleDtoList.size());
        return simpleDtoList;
    }

    /**
     * depth를 통해서 카테고리를 조회하여 카테고리의 기본 정보를 담고있는 dto를 return 하는 기능
     *
     * @param depth 1차는 '0' , 2차는 '1'
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */
    @Override
    public List<CategorySimpleDto> findSimpleDtosByDepth(int depth) {
        QCategory category = QCategory.category;
        return queryFactory.select(new QCategorySimpleDto(
                        category.id,
                        category.name,
                        category.isShown,
                        category.order
                ))
                .from(category)
                .where(category.depth.eq(depth))
                .fetch();
    }

    /**
     * id 를 통해 카테고리를 조회 할 경우, N+1을 해결 하기 위해 fetch join 실행
     *
     * @param id 찾고자하는 카테고리 id
     * @return Optional<Category>
     */
    @Override
    public Optional<Category> findByIdByFetching(Long id) {
        QCategory category = QCategory.category;
        Optional<Category> categoryOptional = Optional.ofNullable(queryFactory.selectFrom(category)
                .leftJoin(category.parent)
                .fetchJoin()
                .where(category.id.eq(id))
                .fetchFirst());

        if (categoryOptional.isEmpty()) {
            return Optional.empty();
        }

        return categoryOptional;
    }


}
