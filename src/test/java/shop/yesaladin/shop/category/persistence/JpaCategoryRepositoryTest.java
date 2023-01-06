package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCategoryRepositoryTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Category sample;
    private final String name = "국내도서";


    @BeforeEach
    void setUp() {
        sample = Category.builder().name(name).order(null).isShown(true).parent(null).build();
    }

    @Test
    void save() {
        //when
        Category save = jpaCategoryRepository.save(sample);

        //then
        assertThat(save.getName()).isEqualTo(name);
        assertThat(save.getParent()).isNull();
    }

    @Test
    void findById() {
        //given
        sample = Category.builder()
                .name(name)
                .order(null)
                .isShown(true)
                .parent(null)
                .build();
        Category save = jpaCategoryRepository.save(sample);

        //when
        Category category = jpaCategoryRepository.findById(save.getId())
                .orElseThrow(() -> new CategoryNotFoundException(save.getId()));

        //then
        assertThat(category.getName()).isEqualTo(save.getName());
        assertThat(category.getId()).isEqualTo(save.getId());
    }

    @Test
    void findAll() {
        //given
        sample = Category.builder()
                .name(name)
                .order(null)
                .isShown(true)
                .parent(null)
                .build();
        Category save = jpaCategoryRepository.save(sample);

        //when
        List<Category> all = jpaCategoryRepository.findAll();

        //then
        assertThat(all.size() > 0).isTrue();
    }

    @Test
    void deleteById() {
        // given
        sample = Category.builder()
                .name(name)
                .order(null)
                .isShown(true)
                .parent(null)
                .build();
        Category save = jpaCategoryRepository.save(sample);

        // when
        jpaCategoryRepository.deleteById(save.getId());
        Category category = jpaCategoryRepository.findById(save.getId()).orElse(null);

        // then
        assertThat(category).isNull();
    }
}

