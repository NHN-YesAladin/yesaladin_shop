package shop.yesaladin.shop.producttag.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.producttag.domain.model.Tag;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTagRepositoryTest {

    @Autowired
    private JpaTagRepository jpaTagRepository;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = Tag.builder().name("감동적인").build();
    }

    @Test
    void save() {
        // when
        Tag savedTag = jpaTagRepository.save(tag);

        // then
        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("감동적인");
    }
}
