package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCategoryRepositoryTest {

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
    void findById() {
        //given
        Category save = jpaCategoryRepository.save(sample);

        //when
        Category category = jpaCategoryRepository.findById(save.getId())
                .orElseThrow(() -> new CategoryNotFoundException(save.getId()));

        //then
        assertThat(category.getName()).isEqualTo(save.getName());
        assertThat(category.getId()).isEqualTo(save.getId());
    }

    @Test
    void findAll_pageable() {
        //given
        int size = 3;
        for (int i = 0; i < 5; i++) {
            sample = Category.builder()
                    .name(name + "1")
                    .order(null)
                    .isShown(true)
                    .parent(null)
                    .build();
            jpaCategoryRepository.save(sample);
        }

        PageRequest pageRequest = PageRequest.of(0, size);

        //when
        Page<Category> page = jpaCategoryRepository.findAll(pageRequest);

        //then
        assertThat(page.getContent().size()).isEqualTo(size);
    }

    @Test
    void deleteById() {
        // given
        Category save = jpaCategoryRepository.save(sample);

        // when
        jpaCategoryRepository.deleteById(save.getId());
        Category category = jpaCategoryRepository.findById(save.getId()).orElse(null);

        // then
        assertThat(category).isNull();
    }

    @Test
    void findByName() {
        // given
        Category save = jpaCategoryRepository.save(sample);

        // when
        Category category = jpaCategoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));

        // then
        assertThat(category.getName()).isEqualTo(save.getName());
    }


}

