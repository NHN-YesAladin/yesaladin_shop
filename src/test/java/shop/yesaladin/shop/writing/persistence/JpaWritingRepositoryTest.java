package shop.yesaladin.shop.writing.persistence;

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
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaWritingRepositoryTest {

    private final String ISBN = "0000000000001";
    private final String NAME = "저자1";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaWritingRepository repository;

    private Product product;
    private Author author;
    private Writing writing;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        product = DummyProduct.dummy(ISBN, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);
        author = DummyAuthor.dummy(NAME, null);

        entityManager.persist(product);
        entityManager.persist(author);

        writing = Writing.create(product, author);
    }

    @Test
    @DisplayName("집필 저장")
    void save() {
        // when
        Writing savedWriting = repository.save(writing);

        // then
        assertThat(savedWriting).isNotNull();
        assertThat(savedWriting.getProduct().getISBN()).isEqualTo(ISBN);
        assertThat(savedWriting.getAuthor().getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("상품으로 집필 삭제")
    void deleteByProduct() {
        // given
        entityManager.persist(writing);

        // when
        repository.deleteByProduct(product);

        // then
        Writing foundWriting = entityManager.find(Writing.class, writing.getPk());
        assertThat(foundWriting).isNull();
    }

    @Test
    @DisplayName("상품으로 집필 조회")
    void findByProduct() {
        // given
        entityManager.persist(writing);

        // when
        List<Writing> foundWritings = repository.findByProduct(product);

        // then
        assertThat(foundWritings).isNotNull();
        assertThat(foundWritings.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품으로 존재 여부 확인_존재 O")
    void existsByProduct_true() {
        // given
        entityManager.persist(writing);

        // when
        boolean isExists = repository.existsByProduct(product);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("상품으로 존재 여부 확인_존재 X")
    void existsByProduct_false() {
        // when
        boolean isExists = repository.existsByProduct(product);

        // then
        assertThat(isExists).isFalse();
    }
}
