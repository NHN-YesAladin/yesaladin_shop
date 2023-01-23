package shop.yesaladin.shop.writing.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaAuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaAuthorRepository repository;

    @Test
    @DisplayName("저자 저장")
    void save() {
        // given
        Author author = DummyAuthor.dummy("저자1", null);

        // when
        Author savedAuthor = repository.save(author);

        // then
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getName()).isEqualTo("저자1");
        assertThat(savedAuthor.getMember()).isNull();
    }

    @Test
    @DisplayName("저자 ID로 조회")
    void findById() {
        // given
        Author author = DummyAuthor.dummy("저자1", null);

        Author savedAuthor = entityManager.persist(author);

        // when
        Optional<Author> optionalAuthor = repository.findById(savedAuthor.getId());

        // then
        assertThat(optionalAuthor).isPresent();
        assertThat(optionalAuthor.get().getName()).isEqualTo(author.getName());
        assertThat(optionalAuthor.get().getMember()).isNull();
    }

    @Test
    @DisplayName("저자 전체 조회")
    void findAll() {
        // given
        Author author1 = DummyAuthor.dummy("저자1", null);
        Author author2 = DummyAuthor.dummy("저자2", null);

        entityManager.persist(author1);
        entityManager.persist(author2);

        // when
        List<Author> authors = repository.findAll();

        // then
        assertThat(authors.contains(author1)).isTrue();
        assertThat(authors.contains(author2)).isTrue();
    }
}