package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.*;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductRepositoryTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    private Product product;
    private SubscribeProduct subscribeProduct;
    private File thumbnailFile;
    private File ebookFile;
    private TotalDiscountRate totalDiscountRate;

    @BeforeEach
    void setUp() {
        subscribeProduct = DummySubscribeProduct.dummy();
        thumbnailFile = DummyFile.dummy(URL + "/image.png");
        ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        product = DummyProduct.dummy(
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
    }

    @Test
    @DisplayName("상품 저장")
    void save() {
        // when
        Product savedProduct = jpaProductRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getISBN()).isEqualTo(ISBN);
        assertThat(savedProduct.getTitle()).isEqualTo("ex_title");
        assertThat(savedProduct.getContents()).isEqualTo("ex_contents");
        assertThat(savedProduct.getDescription()).isEqualTo("ex_description");
        assertThat(savedProduct.getActualPrice()).isEqualTo(10000L);
        assertThat(savedProduct.getDiscountRate()).isEqualTo(0);
        assertThat(savedProduct.isSeparatelyDiscount()).isEqualTo(false);
        assertThat(savedProduct.getGivenPointRate()).isEqualTo(2);
        assertThat(savedProduct.isGivenPoint()).isEqualTo(true);
        assertThat(savedProduct.isSubscriptionAvailable()).isEqualTo(false);
        assertThat(savedProduct.isSale()).isEqualTo(true);
        assertThat(savedProduct.isForcedOutOfStock()).isEqualTo(false);
        assertThat(savedProduct.getQuantity()).isEqualTo(1000L);
        assertThat(savedProduct.getPreferentialShowRanking()).isEqualTo(2);
        assertThat(savedProduct.getSubscribeProduct()).isEqualTo(subscribeProduct);
        assertThat(savedProduct.getThumbnailFile()).isEqualTo(thumbnailFile);
        assertThat(savedProduct.getEbookFile()).isEqualTo(ebookFile);
        assertThat(savedProduct.getProductTypeCode()).isEqualTo(ProductTypeCode.NONE);
        assertThat(savedProduct.getTotalDiscountRate()).isEqualTo(totalDiscountRate);
        assertThat(savedProduct.getProductSavingMethodCode()).isEqualTo(ProductSavingMethodCode.ACTUAL_PURCHASE_PRICE);
    }

}
