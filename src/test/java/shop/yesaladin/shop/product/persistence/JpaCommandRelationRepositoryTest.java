package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCommandRelationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCommandRelationRepository repository;

    private Product productMain;
    private Product productSub;

    @BeforeEach
    void setUp() {
        String isbn1 = "0000000000001";
        String isbn2 = "0000000000002";

        SubscribeProduct subscribeProduct1 = null;
        SubscribeProduct subscribeProduct2 = DummySubscribeProduct.dummy();

        File thumbnailFile1 = DummyFile.dummy("https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type/file1.png");
        File thumbnailFile2 = DummyFile.dummy("https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type/file2.png");

        File ebookFile1 = DummyFile.dummy("https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type/file3.pdf");
        File ebookFile2 = DummyFile.dummy("https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type/file4.pdf");

        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        productMain = DummyProduct.dummy(
                isbn1,
                subscribeProduct1,
                thumbnailFile1,
                ebookFile1,
                totalDiscountRate
        );

        productSub = DummyProduct.dummy(
                isbn2,
                subscribeProduct2,
                thumbnailFile2,
                ebookFile2,
                totalDiscountRate
        );
    }

    @Test
    @DisplayName("상품 연관관계 등록")
    void save() {

    }

    @Test
    @DisplayName("상품 Pk로 삭제")
    void deleteByPk() {

    }
}