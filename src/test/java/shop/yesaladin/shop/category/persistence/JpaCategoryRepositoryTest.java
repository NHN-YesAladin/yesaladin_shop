package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dummy.CategoryDummy;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCategoryRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Category sample;
    private String name = "국내도서";


    @BeforeEach
    void setUp() {
        sample = CategoryDummy.dummyParent();
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
    void deleteById() {
        // given
        Category persistedCategory = em.persist(sample);

        // when
        jpaCategoryRepository.deleteById(persistedCategory.getId());
        Category category = em.find(Category.class, persistedCategory.getId());

        // then
        assertThat(category).isNull();
    }



}

