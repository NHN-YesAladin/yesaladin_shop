package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.publish.domain.model.Publisher;

public class DummyPublisher {

    public static Publisher dummy() {
        return Publisher.builder()
                .name("출판사")
                .build();
    }

    public static Publisher dummy(String name) {
        return Publisher.builder()
                .name(name)
                .build();
    }
}
