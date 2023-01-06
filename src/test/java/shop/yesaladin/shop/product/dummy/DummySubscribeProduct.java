package shop.yesaladin.shop.product.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

public class DummySubscribeProduct {
    public static SubscribeProduct dummy() {
        return SubscribeProduct.builder()
                .id(1L)
                .ISSN("0000-XXXX")
                .build();
    }

}
