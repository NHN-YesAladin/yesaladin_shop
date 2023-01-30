package shop.yesaladin.shop.category.service.inter;

import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.dto.ProductCategoryIdDto;
import shop.yesaladin.shop.category.dto.ProductCategoryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * @author 배수한
 * @since 1.0
 */
public interface CommandProductCategoryService {

    ProductCategoryResponseDto register(ProductCategory productCategory);

    void deleteByProduct(Product product);
}
