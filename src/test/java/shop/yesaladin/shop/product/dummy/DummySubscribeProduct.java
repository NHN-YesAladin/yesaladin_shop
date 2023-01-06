package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

public class DummySubscribeProduct {
    public static SubscribeProduct dummy() {
        return SubscribeProduct.builder()
                .ISSN("0000-XXXX")
                .build();
    }

}
