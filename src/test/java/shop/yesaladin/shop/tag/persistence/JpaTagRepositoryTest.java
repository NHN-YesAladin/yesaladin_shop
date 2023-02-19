package shop.yesaladin.shop.tag.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.tag.domain.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCommandTagRepository repository;

    @Test
    @DisplayName("태그 저장")
    void save() {
        // given
        String name = "행복한";
        Tag tag = Tag.builder().name(name).build();

        // when
        Tag savedTag = repository.save(tag);

        // then
        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(name);
    }
}
