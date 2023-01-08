package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.publisher.domain.model.Publisher;

public class DummyPublisher {

    public static Publisher dummy() {
        return Publisher.builder()
                .name("길벗")
                .build();
    }

}
