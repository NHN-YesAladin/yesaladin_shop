package shop.yesaladin.shop.product.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.publisher.domain.model.Publisher;

public class DummyProduct {

    public static Product dummy(String ISBN) {
        return Product.builder()
                .ISBN(ISBN)
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
                .thumbnailFile(DummyFile.dummy("png"))
                .ebookFile(DummyFile.dummy("pdf"))
                .productTypeCode(DummyProductTypeCode.dummy())
                .totalDiscountRate(DummyTotalDiscountRate.dummy())
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

    public static Product dummy(
            String ISBN,
            SubscribeProduct subscribeProduct,
            Publisher publisher,
            File thumbnailFile,
            File ebookFile,
            TotalDiscountRate totalDiscountRate
    ) {

        return Product.builder()
                .ISBN(ISBN)
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
                .subscribeProduct(subscribeProduct)
                .publisher(publisher)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .productTypeCode(DummyProductTypeCode.dummy())
                .totalDiscountRate(totalDiscountRate)
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

}
