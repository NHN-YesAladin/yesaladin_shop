package shop.yesaladin.shop.category.dummy;


import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.product.domain.model.Product;

public class ProductCategoryDummy {

    public static ProductCategory dummy(Category category, Product product) {
        return new ProductCategory(category, product);
    }
}
