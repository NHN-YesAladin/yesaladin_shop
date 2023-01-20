package shop.yesaladin.shop.tag.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.tag.domain.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTagRepositoryTest {

    private final String TAG_NAME = "감동적인";

    @Autowired
    private JpaTagRepository jpaTagRepository;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = Tag.builder().name(TAG_NAME).build();
    }

    @Disabled
    @Test
    void save() {
        // when
        Tag savedTag = jpaTagRepository.save(tag);

        // then
        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("감동적인");
    }
}
