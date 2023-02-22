package shop.yesaladin.shop.product.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

class ProductTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    private final String ISBN = "0000000000001";

    Product product;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        product = DummyProduct.dummy(
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
    }

    @Test
    @DisplayName("상품 soft delete 성공")
    void deleteProduct_success() {
        // when
        product.deleteProduct();

        // then
        assertThat(product.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("상품 soft delete 실패_이미 삭제된 상품인 경우 예외 발생")
    void deleteProduct_deletedProduct_throwAlreadyDeletedProductException() {
        // given
        product.deleteProduct();

        // when then
        assertThatThrownBy(() -> product.deleteProduct())
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 재고수량 수정 성공")
    void changeQuantity_success() {
        // when
        product.changeQuantity(100);

        // then
        assertThat(product.getQuantity()).isEqualTo(100L);
    }

    @Test
    @DisplayName("상품 재고수량 수정 실패_저장하고자 하는 수량이 음수인 경우")
    void changeQuantity_negativeQuantity_throwNegativeOrZeroQuantityException() {
        // when then
        assertThatThrownBy(() -> product.changeQuantity(-1))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 판매여부 변경 성공")
    void changeIsSale_success() {
        // when
        product.changeIsSale();

        // then
        assertThat(product.isSale()).isFalse();
    }

    @Test
    @DisplayName("상품 강제품절여부 변경 성공")
    void changeIsForcedOutOfStock_success() {
        // when
        product.changeIsForcedOutOfStock();

        // then
        assertThat(product.isForcedOutOfStock()).isTrue();
    }
}