package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

public class DummyTotalDiscountRate {
    public static TotalDiscountRate dummy() {
        return TotalDiscountRate.builder()
                .id(1)
                .discountRate(10)
                .build();
    }
}
