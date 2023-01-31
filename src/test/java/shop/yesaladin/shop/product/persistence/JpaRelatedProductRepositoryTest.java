package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaRelationRepositoryTest {

    private final String ISBN1 = "0000000000001";
    private final String ISBN2 = "0000000000002";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCommandRelationRepository repository;

    private Relation relatedProduct;

    private Product product1;
    private Product product2;


    @BeforeEach
    void setUp() {
        File ebookFile1 = DummyFile.dummy(URL + "/ebook1.pdf");
        File ebookFile2 = DummyFile.dummy(URL + "/ebook2.pdf");
        entityManager.persist(ebookFile1);
        entityManager.persist(ebookFile2);

        File thumbnailFile1 = DummyFile.dummy(URL + "/image1.png");
        File thumbnailFile2 = DummyFile.dummy(URL + "/image2.png");
        entityManager.persist(thumbnailFile1);
        entityManager.persist(thumbnailFile2);


        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        entityManager.persist(subscribeProduct);

        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();
        entityManager.persist(totalDiscountRate);

        product1 = DummyProduct.dummy(
                ISBN1,
                subscribeProduct,
                thumbnailFile1,
                ebookFile1,
                totalDiscountRate
        );
        product2 = DummyProduct.dummy(
                ISBN2,
                subscribeProduct,
                thumbnailFile2,
                ebookFile2,
                totalDiscountRate
        );

        product1 = entityManager.persist(product1);
        product2 = entityManager.persist(product2);

        relatedProduct = DummyRelatedProduct.dummy(product1, product2);
    }

    @Test
    @DisplayName("연관상품 저장")
    void save() {
        // when
        Relation savedRelatedProduct = repository.save(relatedProduct);

        // then
        assertThat(savedRelatedProduct).isNotNull();
        assertThat(savedRelatedProduct.getProductMain().getISBN()).isEqualTo(ISBN1);
        assertThat(savedRelatedProduct.getProductSub().getISBN()).isEqualTo(ISBN2);
    }

}
