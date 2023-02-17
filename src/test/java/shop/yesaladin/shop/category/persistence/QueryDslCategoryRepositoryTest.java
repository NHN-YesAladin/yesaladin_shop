package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslCategoryRepositoryTest {

    @PersistenceContext
    EntityManager em;
    Category parentCategory;
    Category childCategory;
    Long parentId = 10000L;
    @Autowired
    private QueryCategoryRepository queryCategoryRepository;

    @BeforeEach
    void setUp() {
        parentCategory = CategoryDummy.dummyParent(parentId);
        childCategory = CategoryDummy.dummyChild(parentCategory);
    }


    @Test
    void findCategoriesByParentId_pageable() {
        //given
        int size = 5;
        em.persist(parentCategory);
        int max = 10;
        for (int i = 0; i < max; i++) {
            childCategory = CategoryDummy.dummyChild((long) i, parentCategory);
            em.persist(childCategory);
            if (i == 3) {
                childCategory.disableCategory(childCategory.getName());
            }
        }

        PageRequest pageRequest = PageRequest.of(0, size);

        //when
        Page<Category> page = queryCategoryRepository.findCategoriesByParentId(
                pageRequest,
                parentCategory.getId()
        );

        //then
        assertThat(page.getContent()).hasSize(size);
        assertThat(page.getTotalElements()).isEqualTo(max - 1); //1개는 disable 시켰기때문에
    }

    @Test
    void findByName() {
        // given
        em.persist(parentCategory);

        // when
        Category category = queryCategoryRepository.findByName(parentCategory.getName())
                .orElseThrow(() -> new CategoryNotFoundException(parentCategory.getName()));

        // then
        assertThat(category.getName()).isEqualTo(parentCategory.getName());
    }

    @Test
    void getLatestIdByDepth() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestIdByDepth(parentCategory.getDepth());

        // then
        assertThat(onlyId.getId()).isEqualTo(parentCategory.getId());
    }

    @Test
    void getLatestIdByDepth_noPreviousData() throws Exception {
        // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

        // then
        assertThat(onlyId.getId()).isZero();
    }

    @Test
    void getLatestChildIdByDepthAndParentId() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(childCategory);

        // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                childCategory.getDepth(),
                childCategory.getParent().getId()
        );

        // then
        assertThat(onlyId.getId()).isEqualTo(childCategory.getId());

    }

    @Test
    void getLatestChildIdByDepthAndParentId_noPreviousData() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                parentCategory.getId()
        );

        // then
        assertThat(onlyId.getId()).isEqualTo(parentCategory.getId());

    }

    @Test
    void getLatestOrderByDepth() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        int order = queryCategoryRepository.getLatestOrderByDepth(parentCategory.getDepth());

        // then
        assertThat(order).isEqualTo(parentCategory.getOrder());
    }

    @Test
    void getLatestOrderByDepth_noPreviousData() throws Exception {
        // when
        int order = queryCategoryRepository.getLatestOrderByDepth(parentCategory.getDepth());

        // then
        assertThat(order).isZero();
    }

    @Test
    void getLatestChildOrderByDepthAndParentId() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(childCategory);

        // when
        int order = queryCategoryRepository.getLatestChildOrderByDepthAndParentId(
                childCategory.getDepth(),
                childCategory.getParent().getId()
        );

        // then
        assertThat(order).isEqualTo(childCategory.getOrder());

    }

    @Test
    void getLatestChildOrderByDepthAndParentId_noPreviousData() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        int order = queryCategoryRepository.getLatestChildOrderByDepthAndParentId(
                childCategory.getDepth(),
                childCategory.getParent().getId()
        );

        // then
        assertThat(order).isZero();

    }

    @Test
    void findCategories_parentId() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(childCategory);
        em.persist(CategoryDummy.dummyChild(1L, parentCategory));

        // when
        List<Category> categories = queryCategoryRepository.findCategories(childCategory.getParent()
                .getId(), null);

        // then
        assertThat(categories).hasSize(2);
    }

    @Test
    void findCategories_depth() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(CategoryDummy.dummyParent(100L));
        em.persist(childCategory);

        // when
        List<Category> categories = queryCategoryRepository.findCategories(
                null,
                parentCategory.getDepth()
        );

        // then
        assertThat(categories).hasSize(2);
    }

    @Test
    void findCategories_bothNull() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(CategoryDummy.dummyParent(100L));
        em.persist(childCategory);
        em.persist(CategoryDummy.dummyChild(1L, parentCategory));

        // when
        List<Category> categories = queryCategoryRepository.findCategories(null, null);

        // then
        assertThat(categories).hasSize(4);
    }

    @Test
    void findCategories_parentId_wrongDepth() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(CategoryDummy.dummyParent(100L));
        em.persist(childCategory);
        em.persist(CategoryDummy.dummyChild(1L, parentCategory));

        // when
        List<Category> categories = queryCategoryRepository.findCategories(
                parentCategory.getId(),
                Category.DEPTH_PARENT
        );

        // then
        assertThat(categories).isEmpty();
    }

    @Test
    void findCategories_parentId_depth() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(CategoryDummy.dummyParent(100L));
        em.persist(childCategory);
        em.persist(CategoryDummy.dummyChild(1L, parentCategory));

        // when
        List<Category> categories = queryCategoryRepository.findCategories(
                parentCategory.getId(),
                Category.DEPTH_CHILD
        );

        // then
        assertThat(categories).hasSize(2);
    }


    @Test
    void findById() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        Category category = queryCategoryRepository.findById(parentCategory.getId())
                .orElseThrow(() -> new CategoryNotFoundException(parentCategory.getId()));

        // then
        assertThat(category.getId()).isEqualTo(parentCategory.getId());
        assertThat(category.getParent()).isEqualTo(parentCategory.getParent());
    }

    @Disabled("로컬 DB 테스트 용")
    @Test
    void findById_realDB() throws Exception {
        // given
        Long id = 20000L;

        // when
        Category category = queryCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        // then
        assertThat(category.getChildren()).isNotEmpty();
    }

    @Disabled("로컬 DB 테스트 용")
    @Test
    void findCategoriesByParentId_realDB() throws Exception {
        int size = 3;
        // given
        PageRequest pageRequest = PageRequest.of(0, size);

        //when
        Page<Category> page = queryCategoryRepository.findCategoriesByParentId(pageRequest, 10000L);
        log.info("{}", page.getTotalPages());

        //then
        assertThat(page.getContent()).hasSize(size);
    }

}
