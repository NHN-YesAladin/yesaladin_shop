package shop.yesaladin.shop.writing.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslAuthorRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryAuthorRepository repository;

    @Test
    @DisplayName("저자 ID로 조회")
    void findById() {
        // given
        Author author = DummyAuthor.dummy("저자1", null);

        entityManager.persist(author);

        // when
        Optional<Author> optionalAuthor = repository.findById(author.getId());

        // then
        assertThat(optionalAuthor).isPresent();
        assertThat(optionalAuthor.get().getName()).isEqualTo(author.getName());
        assertThat(optionalAuthor.get().getMember()).isNull();
    }

    @Test
    @DisplayName("저자 페이징된 전체 조회")
    void findAllForManager() {
        // given
        for (long i = 1L; i <= 10L; i++) {
            Author author = Author.builder().name("저자" + i).member(null).build();
            entityManager.persist(author);
        }

        // when
        Page<Author> authors = repository.findAllForManager(PageRequest.of(0, 5));

        // then
        assertThat(authors).isNotNull();
        assertThat(authors.getTotalElements()).isEqualTo(10);
        assertThat(authors.getTotalPages()).isEqualTo(2);
    }
}