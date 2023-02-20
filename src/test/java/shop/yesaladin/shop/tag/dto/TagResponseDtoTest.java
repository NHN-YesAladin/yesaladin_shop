package shop.yesaladin.shop.tag.dto;

import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;

class TagResponseDtoTest {

    @Test
    void getTagFromProductTag() {
        // given
        Product product = DummyProduct.dummy("0000000000001", null, DummyFile.dummy("thumbnail"), null, DummyTotalDiscountRate.dummy());
        Tag tag = Tag.builder().id(1L).name("아름다운").build();
        ProductTag productTag = ProductTag.create(product, tag);

        // when
        TagResponseDto.getTagFromProductTag(
                new ProductTagResponseDto(productTag.getPk(), productTag.getProduct(), productTag.getTag())
        );
    }
}