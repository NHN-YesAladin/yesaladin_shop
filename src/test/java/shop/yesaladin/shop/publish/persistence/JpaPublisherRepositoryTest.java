package shop.yesaladin.shop.publish.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPublisherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaPublisherRepository repository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisher = DummyPublisher.dummy();
    }

    @Test
    @DisplayName("출판사 저장")
    void save() {
        // when
        Publisher savedPublisher = repository.save(publisher);

        // then
        assertThat(savedPublisher).isNotNull();
        assertThat(savedPublisher.getName()).isEqualTo("출판사");
    }

    @Test
    @DisplayName("태그 ID로 조회")
    void findById() {
        // given
        Publisher savedPublisher = entityManager.persist(publisher);

        // when
        Optional<Publisher> optionalPublisher = repository.findById(savedPublisher.getId());

        // then
        assertThat(optionalPublisher).isPresent();
        assertThat(optionalPublisher.get().getName()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판사 이름으로 조회")
    void findByName() {
        // given
        Publisher savedPublisher = entityManager.persist(publisher);

        // when
        Optional<Publisher> optionalPublisher = repository.findByName(savedPublisher.getName());

        // then
        assertThat(optionalPublisher).isPresent();
        assertThat(optionalPublisher.get().getId()).isEqualTo(publisher.getId());
    }

    @Test
    @DisplayName("출판사 전체 조회")
    void findAll() {
        // given
        entityManager.persist(publisher);

        // when
        List<Publisher> foundPublishers = repository.findAll();

        // then
        assertThat(foundPublishers.size()).isEqualTo(1);
        assertThat(foundPublishers.contains(publisher)).isTrue();
    }
}
