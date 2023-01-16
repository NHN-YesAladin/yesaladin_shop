package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.dto.ProductCategoryIdDto;

/**
 * @author 배수한
 * @since 1.0
 */
public interface CommandProductCategoryService {

    ProductCategoryIdDto register(ProductCategoryIdDto idDto);

    void delete(Pk pk);
}
