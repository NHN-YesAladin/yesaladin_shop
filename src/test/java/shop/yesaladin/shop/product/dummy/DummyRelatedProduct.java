package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.RelatedProduct;

public class DummyRelatedProduct {

    public static RelatedProduct dummy(Product product1, Product product2) {
        RelatedProduct.Pk pk = RelatedProduct.Pk.builder()
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
