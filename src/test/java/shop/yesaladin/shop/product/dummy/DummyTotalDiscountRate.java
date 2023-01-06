package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

public class DummyTotalDiscountRate {
    public static TotalDiscountRate dummy() {
        return TotalDiscountRate.builder()
                .discountRate(10)
                .build();
    }
}
