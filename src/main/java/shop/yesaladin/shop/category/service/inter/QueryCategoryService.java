package shop.yesaladin.shop.category.service.inter;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoriesSimpleResponseDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;

/**
 * 카테고리 조회용 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryService {

    /**
     * 페이징된 카테고리 리스트 조회를 위한 기능
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 된 CategoryResponse Page 객체
     */
    Page<CategoryResponseDto> findCategories(Pageable pageable);

    /**
     * 모든 카테고리에대한 정보를 Dto를 통해서 필요한 정보만 뽑아서 controller로 전달
     *
     * @return 1차 카테고리에 대한 단건 기본정보와 해당 1차 카테고리의 자식 카테고리들에 대한 기본정보 리스트를 반환
     */
    List<CategoriesSimpleResponseDto> findAllCategoryResponseDto();

    /**
     * 단일 카테고리 조회를 위한 기능
     *
     * @param id 조회하고자 하는 카테고리 id
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    CategoryResponseDto findCategoryById(long id);

    /**
     * 카테고리 id를 통해 카테고리를 조회 하기위한 기능
     *  내부적으로 사용 - 부모 카테고리 조회, update시 영속성 속성 사용하여 성능 개선시 사용
     *
     * @param id 부모 카테고리의 id
     * @return 조회된 부모 Category
     */
    Category findInnerCategoryById(long id);
}
