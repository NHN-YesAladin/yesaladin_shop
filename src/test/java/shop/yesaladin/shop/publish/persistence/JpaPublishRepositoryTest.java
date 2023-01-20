package shop.yesaladin.shop.publish.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPublishRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaPublishRepository jpaPublishRepository;

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-20T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    private Publish publish;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy("00001-...");
        Publisher publisher = DummyPublisher.dummy();

        entityManager.persist(product);
        entityManager.persist(publisher);

        publish = Publish.create(product, publisher, LocalDateTime.now(clock).toLocalDate().toString());
    }

    @Test
    void save() {
        // when
        Publish savedPublish = jpaPublishRepository.save(publish);

        // then
        assertThat(savedPublish).isNotNull();
    }
}
