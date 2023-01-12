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
    void findCategoriesByParentId_pageable() {
        //given
        int size = 3;
        em.persist(parentCategory);
        for (int i = 0; i < 10; i++) {
            childCategory = CategoryDummy.dummyChild((long) i, parentCategory);
            em.persist(childCategory);
        }

        PageRequest pageRequest = PageRequest.of(1, size);

        //when
        Page<Category> page = queryCategoryRepository.findCategoriesByParentId(pageRequest,
                parentCategory.getId());
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
        assertThat(onlyId.getId()).isEqualTo(0L);
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
    void findCategories_parentId() throws Exception {
        // given
        em.persist(parentCategory);
        em.persist(childCategory);
        em.persist(CategoryDummy.dummyChild(1L, parentCategory));

        // when
        List<Category> categories = queryCategoryRepository.findCategories(childCategory.getParent()
                .getId(), null);

        // then
        assertThat(categories.size()).isEqualTo(2);
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
        assertThat(categories.size()).isEqualTo(2);
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
        assertThat(categories.size()).isEqualTo(4);
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
        assertThat(categories.size()).isEqualTo(0);
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
        assertThat(categories.size()).isEqualTo(2);
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
        assertThat(category.getChildren().size() > 0).isTrue();
        System.out.println("category.getChildren() = " + category.getChildren());
    }

}
