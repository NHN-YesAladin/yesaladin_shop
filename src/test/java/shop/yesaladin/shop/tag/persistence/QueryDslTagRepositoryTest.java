package shop.yesaladin.shop.tag.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;


@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslTagRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryTagRepository repository;

    @Test
    @DisplayName("태그 ID로 조회")
    void findById() {
        // given
        String name = "아름다운";
        Tag tag = Tag.builder().name(name).build();

        entityManager.persist(tag);

        // when
        Optional<Tag> optionalTag = repository.findById(tag.getId());

        // then
        assertThat(optionalTag).isPresent();
        assertThat(optionalTag.get().getName()).isEqualTo(tag.getName());
    }

    @Test
    @DisplayName("태그 전체 조회")
    void findAll() {
        // given
        Tag tag1 = Tag.builder().name("아름다운").build();
        Tag tag2 = Tag.builder().name("슬픈").build();

        entityManager.persist(tag1);
        entityManager.persist(tag2);

        // when
        List<Tag> tags = repository.findAll();

        // then
        assertThat(tags).contains(tag1);
        assertThat(tags).contains(tag2);
    }

    @Test
    @DisplayName("태그 페이징된 전체 조회")
    void findAllForManager() {
        // given
        for (long i = 1L; i <= 10L; i++) {
            Tag tag = Tag.builder().name("태그" + i).build();
            entityManager.persist(tag);
        }

        // when
        Page<Tag> tags = repository.findAllForManager(PageRequest.of(0, 5));

        // then
        assertThat(tags).isNotNull();
        assertThat(tags.getTotalElements()).isEqualTo(10);
        assertThat(tags.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("이미 존재하는 태그인 경우 확인 성공")
    void existsByName_exists() {
        // given
        String name = "아름다운";
        Tag tag = Tag.builder().name(name).build();
        entityManager.persist(tag);

        // when
        boolean isExists = repository.existsByName(name);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("이미 존재하는 태그가 아닌 경우 확인 성공")
    void existsByName_notExists() {
        // when
        boolean isExists = repository.existsByName("태그");

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("")
    void findByNameForManager() {
        Tag tag = Tag.builder().name("tag").build();
        entityManager.persist(tag);

        Page<Tag> result = repository.findByNameForManager("t", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getName()).contains("t");
    }
}