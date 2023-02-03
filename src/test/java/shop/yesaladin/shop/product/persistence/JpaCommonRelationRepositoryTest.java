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
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaCommonRelationRepositoryTest {

    private final String ISBN1 = "0000000000001";
    private final String ISBN2 = "0000000000002";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCommandRelationRepository repository;

    private Relation relation;

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
                null,
                thumbnailFile2,
                ebookFile2,
                totalDiscountRate
        );

        product1 = entityManager.persist(product1);
        product2 = entityManager.persist(product2);

        relation = Relation.create(product1, product2);
    }

    @Test
    @DisplayName("연관상품 저장")
    void save() {
        // when
        Relation savedRelatedProduct = repository.save(relation);

        // then
        assertThat(savedRelatedProduct).isNotNull();
        assertThat(savedRelatedProduct.getProductMain().getIsbn()).isEqualTo(ISBN1);
        assertThat(savedRelatedProduct.getProductSub().getIsbn()).isEqualTo(ISBN2);
    }

    @Test
    @DisplayName("연관상품 PK로 삭제")
    void deleteByPk() {
        // given
        entityManager.persist(relation);

        // when
        repository.deleteByPk(relation.getPk());

        // then
        Relation foundRelation = entityManager.find(Relation.class, relation.getPk());
        assertThat(foundRelation).isNull();
    }
}
