package shop.yesaladin.shop.publish.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class QueryDslPublisherRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryPublisherRepository repository;

    @Test
    @DisplayName("출판사 ID로 조회")
    void findById() {
        // given
        Publisher publisher = DummyPublisher.dummy("출판사1");

        entityManager.persist(publisher);

        // when
        Optional<Publisher> optionalPublisher = repository.findById(publisher.getId());

        // then
        assertThat(optionalPublisher).isPresent();
        assertThat(optionalPublisher.get().getName()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판사 전체 조회")
    void findAll() {
        // given
        Publisher publisher1 = DummyPublisher.dummy("출판사1");
        Publisher publisher2 = DummyPublisher.dummy("출판사2");

        entityManager.persist(publisher1);
        entityManager.persist(publisher2);

        // when
        List<Publisher> publishers = repository.findAll();

        // then
        assertThat(publishers).contains(publisher1);
        assertThat(publishers).contains(publisher2);
    }

    @Test
    @DisplayName("출판사 페이징된 전체 조회")
    void findAllForManager() {
        // given
        for (long i = 1L; i <= 10L; i++) {
            Publisher publisher = Publisher.builder().name("출판사" + i).build();
            entityManager.persist(publisher);
        }

        // when
        Page<Publisher> publishers = repository.findAllForManager(PageRequest.of(0, 5));

        // then
        assertThat(publishers).isNotNull();
        assertThat(publishers.getTotalElements()).isEqualTo(10);
        assertThat(publishers.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("이미 존재하는 출판사인 경우 확인 성공")
    void existsByName_exists() {
        // given
        Publisher publisher = DummyPublisher.dummy();
        entityManager.persist(publisher);

        // when
        boolean isExists = repository.existsByName("출판사");

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("이미 존재하는 출판사가 아닌 경우 확인 성공")
    void existsByName_notExists() {
        // when
        boolean isExists = repository.existsByName("출판사");

        // then
        assertThat(isExists).isFalse();
    }
}