package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;

/**
 * 카테고리 조회용(R) 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryRepository {

    /**
     * 카테고리 paging list 조회
     *
     * @param pageable size 와 page 를 가진 객체
     * @return paging 되어있는 Category Page 객체
     */
    Page<Category> findCategoriesByParentId(Pageable pageable, Long parentId);


    /**
     * 카테고리 이름을 통한 카테고리 조회
     *
     * @param name 카테고리 이름
     * @return Optional 처리가 된 카테고리 객체
     */
    Optional<Category> findByName(String name);

    /**
     * 카테고리 id의 마지막 값을 depth와 부모 id를 통해 조회 2차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth    2차 카테고리의 깊이 값인 1이 입력됨
     * @param parentId 2차 카테고리가 가지고있는 부모 id
     * @return Long id 만 가지고있음
     */
    CategoryOnlyIdDto getLatestChildIdByDepthAndParentId(int depth, Long parentId);

    /**
     * 카테고리의 id의 마지막 값을 depth를 통해 조회 1차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 1차 카테고리의 깊이 값인 0이 입력됨
     * @return Long id 만 가지고있음
     */
    CategoryOnlyIdDto getLatestIdByDepth(int depth);

    /**
     * 카테고리 order의 마지막 값을 depth와 부모 id를 통해 조회
     *   2차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth    2차 카테고리의 깊이 값인 1이 입력됨
     * @param parentId 2차 카테고리가 가지고있는 부모 id
     * @return Long id 만 가지고있음
     */
    int getLatestChildOrderByDepthAndParentId(int depth, Long parentId);
    /**
     * 카테고리의 order의 마지막 값을 depth를 통해 조회
     *   1차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 1차 카테고리의 깊이 값인 0이 입력됨
     * @return Long id 만 가지고있음
     */
    int getLatestOrderByDepth(int depth);

    /**
     * parentId 혹은 depth를 통해 category를 찾을 때 사용
     *
     * @param parentId 찾고자하는 카테고리의 parentId
     * @param depth    찾고자하는 카테고리의 depth
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */

    List<Category> findCategories(Long parentId, Integer depth);


    /**
     * id 를 통해 카테고리를 조회 할 경우, N+1을 해결 하기 위해 fetch join 실행
     *
     * @param id 찾고자하는 카테고리 id
     * @return Optional<Category>
     */
    Optional<Category> findById(Long id);

}
