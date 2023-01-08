package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryOnlyId;
import shop.yesaladin.shop.category.dto.CategoryResponse;

/**
 * Create, Update, Delete 를 controller 단에서 사용하기 위해 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryService {

    CategoryResponse create(CategoryRequest createRequest);

    CategoryResponse update(Long id, CategoryRequest createRequest);

    void delete(CategoryOnlyId onlyId);
}
