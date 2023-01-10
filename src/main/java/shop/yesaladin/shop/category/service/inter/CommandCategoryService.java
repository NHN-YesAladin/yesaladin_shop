package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;

/**
 * Create, Update, Delete 를 controller 단에서 사용하기 위해 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryService {

    /**
     * 카테고리 생성을 위한 기능
     *
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    CategoryResponseDto create(CategoryRequestDto createRequest);

    /**
     * 카테고리 수정을 위한 기능
     *
     * @param id 수정하고자 하는 카테고리 id
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    CategoryResponseDto update(Long id, CategoryRequestDto createRequest);


    /**
     * 카테고리 삭제를 위한 기능
     *
     * @param id 삭제하고자 하는 카테고리 id
     */
    void delete(Long id);
}
