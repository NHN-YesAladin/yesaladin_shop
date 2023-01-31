package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;

public class DummyRelatedProduct {

    public static Relation dummy(Product product1, Product product2) {
        Relation.Pk pk = Relation.Pk.builder()
                .productMainId(product1.getId())
                .productSubId(product2.getId())
                .build();

        return Relation.builder()
                .pk(pk)
                .productMain(product1)
                .productSub(product2)
                .build();
    }
}
