package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;

/**
 * Create, Update, Delete 를 controller 단에서 사용하기 위해 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryService {

    Category create(CategoryCreateDto createDto);

    Category update(CategoryUpdateDto updateDto);

}
