package shop.yesaladin.shop.category.exception;

import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;

/**
 * 찾고자하는 ProductCategory 가 없을때 발생하는 예외
 *
 * @author 배수한
 * @since 1.0
 */
public class ProductCategoryNotFoundException extends RuntimeException{

    public ProductCategoryNotFoundException(Pk pk) {
        super(new StringBuilder().append("ProductCategory not found : ")
                .append("categoryId = ")
                .append(pk.getCategoryId())
                .append(", ")
                .append("productId = ")
                .append(pk.getProductId())
                .toString());

    }
}
