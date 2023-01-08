package shop.yesaladin.shop.category.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryResponse;

/**
 * 카테고리 조회용 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryService {

    Page<CategoryResponse> findCategories(Pageable pageable);

    CategoryResponse findCategoryById(long id);

    Category findParentCategoryById(long id);
}
