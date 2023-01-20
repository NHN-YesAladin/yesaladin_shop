package shop.yesaladin.shop.publish.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPublisherRepositoryTest {

    private final String PUBLISHER_NAME = "길벗";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaPublisherRepository jpaPublisherRepository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisher = DummyPublisher.dummy();
    }

    @Test
    void save() {
        // when
        Publisher savedPublisher = jpaPublisherRepository.save(publisher);

        // then
        assertThat(savedPublisher).isNotNull();
        assertThat(savedPublisher.getName()).isEqualTo(PUBLISHER_NAME);
    }

    @Test
    void findByName() {
        // given
        entityManager.persist(publisher);

        // when
        Optional<Publisher> foundPublisher = jpaPublisherRepository.findByName(publisher.getName());

        // then
        assertThat(foundPublisher).isPresent();
        assertThat(foundPublisher.get().getName()).isEqualTo(publisher.getName());
    }
}
