package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 조회용 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryService {
    Category findCategoryById(long id);
}
