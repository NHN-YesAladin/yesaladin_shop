package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;

public class DummyProductSavingMethodCode {
    public static ProductSavingMethodCode dummy() {
        return ProductSavingMethodCode.ACTUAL_PURCHASE_PRICE;
    }
}
