package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;

@Slf4j
@Transactional
@SpringBootTest
class QueryDslCategoryRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private QueryCategoryRepository queryCategoryRepository;

    Category parentCategory;
    Category childCategory;
    Long parentId = 10000L;
    Long childId = 10100L;

    @BeforeEach
    void setUp() {
        parentCategory = CategoryDummy.dummyParent(parentId);
        childCategory = CategoryDummy.dummyChild(parentCategory);
    }

    @Test
    void findById() {
        //given
        em.persist(parentCategory);

        //when
        Category category = queryCategoryRepository.findById(parentCategory.getId())
                .orElseThrow(() -> new CategoryNotFoundException(parentCategory.getId()));

        //then
        assertThat(category.getName()).isEqualTo(parentCategory.getName());
        assertThat(category.getId()).isEqualTo(parentCategory.getId());
    }

    @Test
    void findAll_pageable() {
        //given
        int size = 3;
        for (int i = 0; i < 10; i++) {
            parentCategory = Category.builder()
                    .id((long)i)
                    .name("" + i)
                    .order(null)
                    .isShown(true)
                    .parent(null)
                    .build();
            em.persist(parentCategory);
        }

        PageRequest pageRequest = PageRequest.of(1, size);

        //when
        Page<Category> page = queryCategoryRepository.findAll(pageRequest);
        log.info("{}", page.getContent().size());

        //then
        assertThat(page.getContent().size()).isEqualTo(size);
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
    void getIdByDepth() throws Exception {
        // given
        em.persist(parentCategory);

         // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestIdByDepth(parentCategory.getDepth());

         // then
        assertThat(onlyId.getId()).isEqualTo(parentCategory.getId());
    }

    @Test
    void getIdByDepth_noPreviousData() throws Exception {
        // when
        CategoryOnlyIdDto onlyId = queryCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

        // then
        assertThat(onlyId.getId()).isEqualTo(0L);
    }

    @Test
    void getIdByDepthAndParentId() throws Exception {
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
    void getIdByDepthAndParentId_noPreviousData() throws Exception {
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
    void getCategoriesByParentId() throws Exception {
        // given
        for (int i = 0; i < 10; i++) {
            Category child = Category.builder()
                    .id((long)i)
                    .name("" + i)
                    .order(null)
                    .isShown(true)
                    .parent(parentCategory)
                    .build();
            em.persist(child);
        }

        // when
        queryCategoryRepository.getCategoriesByParentId(parentCategory.getId());

        // then
    }
}
