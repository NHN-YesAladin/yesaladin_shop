package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

public class DummyProduct {

    public static Product dummy(
            String ISBN,
            SubscribeProduct subscribeProduct,
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
                .preferentialShowRanking(2)
                .subscribeProduct(subscribeProduct)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .productTypeCode(ProductTypeCode.NONE)
                .totalDiscountRate(totalDiscountRate)
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

    public static Product dummy(
            String ISBN,
            SubscribeProduct subscribeProduct,
            File thumbnailFile,
            File ebookFile,
            TotalDiscountRate totalDiscountRate,
            ProductTypeCode productTypeCode
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
                .preferentialShowRanking(2)
                .subscribeProduct(subscribeProduct)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .productTypeCode(productTypeCode)
                .totalDiscountRate(totalDiscountRate)
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

    public static Product dummy(
            Long id,
            String ISBN,
            SubscribeProduct subscribeProduct,
            File thumbnailFile,
            File ebookFile,
            TotalDiscountRate totalDiscountRate
    ) {
        return Product.builder()
                .id(id)
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
                .preferentialShowRanking(2)
                .subscribeProduct(subscribeProduct)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .productTypeCode(ProductTypeCode.NONE)
                .totalDiscountRate(totalDiscountRate)
                .productSavingMethodCode(DummyProductSavingMethodCode.dummy())
                .build();
    }

}
