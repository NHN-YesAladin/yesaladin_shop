package shop.yesaladin.shop.tag.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.tag.domain.model.Tag;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaTagRepository repository;

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

    @Test
    @DisplayName("태그 ID로 조회")
    void findById() {
        // given
        String name = "행복한";
        Tag tag = Tag.builder().name(name).build();

        Tag savedTag = entityManager.persist(tag);

        // when
        Optional<Tag> optionalTag = repository.findById(savedTag.getId());

        // then
        assertThat(optionalTag).isPresent();
        assertThat(optionalTag.get().getName()).isEqualTo(tag.getName());
    }

    @Test
    @DisplayName("태그 이름으로 조회")
    void findByName() {
        // given
        String name = "행복한";
        Tag tag = Tag.builder().name(name).build();

        Tag savedTag = entityManager.persist(tag);

        // when
        Optional<Tag> optionalTag = repository.findByName(savedTag.getName());

        // then
        assertThat(optionalTag).isPresent();
        assertThat(optionalTag.get().getId()).isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("태그 전체 조회")
    void findAll() {
        // given
        String name1 = "행복한";
        String name2 = "슬픈";

        Tag tag1 = Tag.builder().name(name1).build();
        Tag tag2 = Tag.builder().name(name2).build();

        entityManager.persist(tag1);
        entityManager.persist(tag2);

        // when
        List<Tag> foundTags = repository.findAll();

        // then
        assertThat(foundTags.contains(tag1)).isTrue();
        assertThat(foundTags.contains(tag2)).isTrue();
    }
}
