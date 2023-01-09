package shop.yesaladin.shop.category.domain.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryDslCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.persistence.JpaCategoryRepository;

@SpringBootTest
class QueryDslCategoryRepositoryImplTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private QueryDslCategoryRepository queryDslCategoryRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getIdByDepth() throws Exception {
        // given
        Category parent = jpaCategoryRepository.save(CategoryDummy.dummyParent());

         // when
        CategoryOnlyIdDto onlyId = queryDslCategoryRepository.getLatestIdByDepth(parent.getDepth());

         // then
        assertThat(onlyId.getId()).isEqualTo(parent.getId());
    }

    @Test
    void getIdByDepth_noPreviousData() throws Exception {
        // when
        CategoryOnlyIdDto onlyId = queryDslCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

        // then
        assertThat(onlyId.getId()).isEqualTo(0L);
    }

    @Test
    void getIdByDepthAndParentId() throws Exception {
        // given
        Category parent = jpaCategoryRepository.save(CategoryDummy.dummyParent());
        Category child = jpaCategoryRepository.save(CategoryDummy.dummyChild(2L, parent));

        // when
        CategoryOnlyIdDto onlyId = queryDslCategoryRepository.getLatestChildIdByDepthAndParentId(
                child.getDepth(),
                child.getParent().getId()
        );

        // then
        assertThat(onlyId.getId()).isEqualTo(child.getId());

    }

    @Test
    void getIdByDepthAndParentId_noPreviousData() throws Exception {
        // given
        Category parent = jpaCategoryRepository.save(CategoryDummy.dummyParent());

        // when
        CategoryOnlyIdDto onlyId = queryDslCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                parent.getId()
        );

        // then
        assertThat(onlyId.getId()).isEqualTo(parent.getId());

    }
}
