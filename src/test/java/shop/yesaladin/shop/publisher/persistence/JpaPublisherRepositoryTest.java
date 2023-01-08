package shop.yesaladin.shop.publisher.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publisher.domain.model.Publisher;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPublisherRepositoryTest {

    private final String PUBLISHER_NAME = "길벗";

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

}
