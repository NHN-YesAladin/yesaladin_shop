package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.RelatedProduct;
import shop.yesaladin.shop.product.domain.model.RelatedProduct.Pk;

public class DummyRelatedProduct {

    public static RelatedProduct dummy(Product product1, Product product2) {
        Pk pk = Pk.builder()
                .productMainId(product1.getId())
                .productSubId(product2.getId())
                .build();

        return RelatedProduct.builder()
                .pk(pk)
                .productMain(product1)
                .productSub(product2)
                .build();
    }
}
