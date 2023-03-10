package shop.yesaladin.shop.category.service.inter;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;

/**
 * 카테고리 조회용 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryService {

    /**
     * 페이징된 2차 카테고리 리스트 조회를 위한 기능
     *
     * @param pageable 페이징 처리를 위한 객체
     * @param parentId 2차 카테고리의 부모 카테고리의 id
     * @return 페이징 된 CategoryResponse Page 객체
     */
    Page<CategoryResponseDto> findCategoriesByParentId(Pageable pageable, Long parentId);

    /**
     * 1차 카테고리를 조회 하기 위한 기능
     *
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    List<CategoryResponseDto> findParentCategories();

    /**
     * 1차 카테고리 id에 해당하는 모든 2차 카테고리를 조회 하는 기능 (paging x)
     *
     * @param parentId 2차 카테고리의 부모 카테고리의 id
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    List<CategoryResponseDto> findChildCategoriesByParentId(Long parentId);


    /**
     * 단일 카테고리 조회를 위한 기능
     *
     * @param id 조회하고자 하는 카테고리 id
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    CategoryResponseDto findCategoryById(long id);

}
