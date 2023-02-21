package shop.yesaladin.shop.publish.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPublishRepositoryTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-20T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private JpaPublishRepository repository;
    private Product product;
    private Publisher publisher;
    private Publish publish;

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

        product = DummyProduct.dummy(
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        publisher = DummyPublisher.dummy();

        entityManager.persist(product);
        entityManager.persist(publisher);

        publish = Publish.create(
                product,
                publisher,
                LocalDateTime.now(clock).toLocalDate().toString()
        );
    }

    @Test
    @DisplayName("출판 관계 저장")
    void save() {
        // when
        Publish savedPublish = repository.save(publish);

        // then
        assertThat(savedPublish).isNotNull();
        assertThat(savedPublish.getProduct().getIsbn()).isEqualTo(ISBN);
        assertThat(savedPublish.getPublisher()).isEqualTo(publisher);
    }

    @Test
    @DisplayName("상품으로 출판 관계 조회")
    void findByProduct() {
        // when
        entityManager.persist(publish);

        Optional<Publish> foundPublish = repository.findByProduct(product);

        // then
        assertThat(foundPublish).isPresent();
        assertThat(foundPublish.get().getPublisher()).isEqualTo(publisher);
        assertThat(foundPublish.get().getProduct()).isEqualTo(product);
        assertThat(foundPublish.get().getProduct().getIsbn()).isEqualTo(ISBN);
    }

    @Test
    @DisplayName("상품으로 출판 관계 삭제")
    void deleteByProduct() {
        // given
        entityManager.persist(publish);

        // when
        repository.deleteByProduct(product);

        // then
        Publish foundPublish = entityManager.find(Publish.class, publish.getPk());
        assertThat(foundPublish).isNull();
    }
}
