package shop.yesaladin.shop.writing.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaAuthorRepositoryTest {

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
}