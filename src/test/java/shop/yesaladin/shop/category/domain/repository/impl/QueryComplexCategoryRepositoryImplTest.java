package shop.yesaladin.shop.category.domain.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryComplexCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.persistence.JpaCategoryRepository;

@SpringBootTest
class QueryComplexCategoryRepositoryImplTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private QueryComplexCategoryRepository queryComplexCategoryRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getIdByDepth() throws Exception {
        // given
        Category parent = jpaCategoryRepository.save(CategoryDummy.dummyParent());

         // when
        CategoryOnlyIdDto onlyId = queryComplexCategoryRepository.getLatestIdByDepth(parent.getDepth());

         // then
        assertThat(onlyId.getId()).isEqualTo(parent.getId());
    }

    @Test
    void getIdByDepth_noPreviousData() throws Exception {
        // when
        CategoryOnlyIdDto onlyId = queryComplexCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

        // then
        assertThat(onlyId.getId()).isEqualTo(0L);
    }

    @Test
    void getIdByDepthAndParentId() throws Exception {
        // given
        Category parent = jpaCategoryRepository.save(CategoryDummy.dummyParent());
        Category child = jpaCategoryRepository.save(CategoryDummy.dummyChild(2L, parent));

        // when
        CategoryOnlyIdDto onlyId = queryComplexCategoryRepository.getLatestChildIdByDepthAndParentId(
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
        CategoryOnlyIdDto onlyId = queryComplexCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                parent.getId()
        );

        // then
        assertThat(onlyId.getId()).isEqualTo(parent.getId());

    }
}
