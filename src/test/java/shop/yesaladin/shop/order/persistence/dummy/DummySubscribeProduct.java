package shop.yesaladin.shop.order.persistence.dummy;

import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

public class DummySubscribeProduct {

    public static SubscribeProduct subscribeProduct() {
        return SubscribeProduct.builder()
                .ISSN("3924-1232")
                .build();
    }
}
