package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategorySimpleDto;

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
    Page<Category> findAll(Pageable pageable);

    /**
     * id를 통한 카테고리 조회
     *
     * @param id 카테고리 id
     * @return Optional 처리가 된 카테고리 객체
     */
    Optional<Category> findById(Long id);

    /**
     * 카테고리 이름을 통한 카테고리 조회
     *
     * @param name 카테고리 이름
     * @return Optional 처리가 된 카테고리 객체
     */
    Optional<Category> findByName(String name);

    /**
     * 카테고리 id의 마지막 값을 depth와 부모 id를 통해 조회
     *   2차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 2차 카테고리의 깊이 값인 1이 입력됨
     * @param parentId 2차 카테고리가 가지고있는 부모 id
     * @return Long id 만 가지고있음
     */
    CategoryOnlyIdDto getLatestChildIdByDepthAndParentId(int depth, Long parentId);

    /**
     * 카테고리의 id의 마지막 값을 depth를 통해 조회
     *   1차 카테고리의 마지막 id를 찾아오기 위해 사용
     *
     * @param depth 1차 카테고리의 깊이 값인 0이 입력됨
     * @return Long id 만 가지고있음
     */
    CategoryOnlyIdDto getLatestIdByDepth(int depth);

    /**
     * parentId 를 통해서 DtoProjection 으로 데이터를 선별하여 return 하는 기능
     *
     * @param parentId 찾고자하는 카테고리의 parentId
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */
    List<CategorySimpleDto> findSimpleDtosByParentId(Long parentId);

    /**
     * depth를 통해서 카테고리를 조회하여 카테고리의 기본 정보를 담고있는 dto를 return 하는 기능
     *
     * @param depth 1차는 '0' , 2차는 '1'
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */
    List<CategorySimpleDto> findSimpleDtosByDepth(int depth);

    /**
     * id 를 통해 카테고리를 조회 할 경우, N+1을 해결 하기 위해 fetch join 실행
     *
     * @param id 찾고자하는 카테고리 id
     * @return Optional<Category>
     */
    Optional<Category> findByIdByFetching(Long id);

}
