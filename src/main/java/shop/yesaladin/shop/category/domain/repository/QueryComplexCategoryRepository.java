package shop.yesaladin.shop.category.domain.repository;

import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;

/**
 * @author 배수한
 * @since 1.0
 */
public interface QueryComplexCategoryRepository {

    CategoryOnlyIdDto getLatestChildIdByDepthAndParentId(int depth, Long parentId);
    CategoryOnlyIdDto getLatestIdByDepth(int depth);
}
