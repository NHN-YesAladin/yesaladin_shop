package shop.yesaladin.shop.product.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.product.domain.model.Product;

public class DummyProduct {
    public static Product dummy() {
        return Product.builder()
                .id(1L)
                .ISBN("00000-000XX-XXX-XXX")
                .title("ex_title")
                .contents("ex_contents")
                .description("ex_description")
                .actualPrice(10000L)
                .discountRate(0)
                .isSeparatelyDiscount(false)
                .givenPointRate(2)
                .isGivenPoint(true)
                .isSubscriptionAvailable(false)
                .isSale(true)
                .isForcedOutOfStock(false)
                .quantity(1000L)
                .publishedDate(LocalDate.now())
                .preferentialShowRanking(2)
                .subscribeProduct(DummySubscribeProduct.dummy())
                .publisher(DummyPublisher.dummy())
                .file(DummyFile.dummy())
                .productTypeCode(DummyProductTypeCode.dummy())
                .totalDiscountRate(DummyTotalDiscountRate.dummy())
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

}
