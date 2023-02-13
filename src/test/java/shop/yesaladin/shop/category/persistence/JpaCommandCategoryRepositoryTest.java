package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dummy.CategoryDummy;


@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCommandCategoryRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    private JpaCommandCategoryRepository jpaCommandCategoryRepository;

    private Category sample;
    private String name = "국내도서";


    @BeforeEach
    void setUp() {
        sample = CategoryDummy.dummyParent();
    }

    @Test
    void save() {
        //when
        Category save = jpaCommandCategoryRepository.save(sample);

        //then
        assertThat(save.getName()).isEqualTo(name);
        assertThat(save.getParent()).isNull();
    }

    @Test
    void deleteById() {
        // given
        Category persistedCategory = em.persist(sample);

        // when
        jpaCommandCategoryRepository.deleteById(persistedCategory.getId());
        Category category = em.find(Category.class, persistedCategory.getId());

        // then
        assertThat(category).isNull();
    }


}

