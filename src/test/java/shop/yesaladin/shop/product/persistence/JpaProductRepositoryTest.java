package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

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

    Clock clock = Clock.fixed(
            Instant.parse("2023-03-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    private Product product;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png", LocalDateTime.now(clock));
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf", LocalDateTime.now(clock));
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

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
    void save() {
        // when
        Product savedProduct = jpaProductRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getISBN()).isEqualTo(ISBN);
    }

    @Test
    void findByISBN() {
        // given
        entityManager.persist(product);

        // when
        Optional<Product> foundProduct = jpaProductRepository.findByISBN(product.getISBN());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getISBN()).isEqualTo(ISBN);
    }

}
