package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategorySimpleDto;
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
    void findSimpleDtosByParentId() throws Exception {
        // given
        em.persist(parentCategory);
        List<Category> children = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            Category child = Category.builder()
                    .id((long)i)
                    .name("" + i)
                    .order(null)
                    .isShown(true)
                    .parent(parentCategory)
                    .build();
            children.add(child);
            em.persist(child);
        }

        em.flush();

        // when
        List<CategorySimpleDto> categories = queryCategoryRepository.findSimpleDtosByParentId(
                parentCategory.getId());

        // then
        assertThat(categories.size()).isEqualTo(count);
        assertThat(categories.get(0).getId()).isEqualTo(children.get(0).getId());
    }

    @Test
    void findByIdByFetching() throws Exception {
        // given
        em.persist(parentCategory);

        // when
        Category category = queryCategoryRepository.findByIdByFetching(parentCategory.getId())
                .orElseThrow(() -> new CategoryNotFoundException(parentCategory.getId()));

        // then
        assertThat(category.getId()).isEqualTo(parentCategory.getId());
        assertThat(category.getParent()).isEqualTo(parentCategory.getParent());
    }

    @Disabled
    @Test
    @DisplayName("로컬 DB에서 테스트용 - @disabled")
    void findByIdByFetching_realDB() throws Exception {
        // given
        Long id = 20000L;

        // when
        Category category = queryCategoryRepository.findByIdByFetching(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        // then
        assertThat(category.getChildren().size() > 0).isTrue();
        System.out.println("category.getChildren() = " + category.getChildren());
    }

    @Test
    void findSimpleDtosByDepth() throws Exception {
        // given
        em.persist(parentCategory);
        List<Category> children = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            Category child = Category.builder()
                    .id((long)i)
                    .name("" + i)
                    .order(null)
                    .isShown(true)
                    .parent(parentCategory)
                    .depth(Category.DEPTH_CHILD)
                    .build();
            children.add(child);
            em.persist(child);
        }

        // when
        List<CategorySimpleDto> simpleDtosByDepth = queryCategoryRepository.findSimpleDtosByDepth(
                Category.DEPTH_CHILD);

        // then
        assertThat(simpleDtosByDepth.size()).isEqualTo(count);
        assertThat(simpleDtosByDepth.get(0).getId()).isEqualTo(children.get(0).getId());
        assertThat(simpleDtosByDepth.get(0).getName()).isEqualTo(children.get(0).getName());
    }
}
