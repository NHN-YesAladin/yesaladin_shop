package shop.yesaladin.shop.publish.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

    private Publish publish;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy("00001-...");
        Publisher publisher = DummyPublisher.dummy();
        LocalDateTime now = LocalDateTime.now();

        entityManager.persist(product);
        entityManager.persist(publisher);

        publish = Publish.create(product, publisher, now.toLocalDate().toString());
    }

    @Test
    void save() {
        // when
        Publish savedPublish = jpaPublishRepository.save(publish);

        // then
        assertThat(savedPublish).isNotNull();
    }
}
