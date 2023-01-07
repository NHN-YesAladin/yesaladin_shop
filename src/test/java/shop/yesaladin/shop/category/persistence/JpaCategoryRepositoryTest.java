package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.domain.model.Category;
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
        sample = Category.builder().name(name).order(null).isShown(true).parent(null).build();
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
        sample = Category.builder().name(name).order(null).isShown(true).parent(null).build();
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
        sample = Category.builder().name(name).order(null).isShown(true).parent(null).build();
        Category save = jpaCategoryRepository.save(sample);

        // when
        Category category = jpaCategoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));

        // then
        assertThat(category.getName()).isEqualTo(save.getName());
    }

    @Test
    void findByParent_Name() throws Exception {
        // given
        sample = Category.builder().name(name).order(null).isShown(true).parent(null).build();
        Category savedParent = jpaCategoryRepository.save(sample);

        String childName = "소설";
        Category child = Category.builder()
                .name(childName)
                .order(null)
                .isShown(true)
                .parent(savedParent)
                .build();
        Category savedChild = jpaCategoryRepository.save(child);

        // when
        List<Category> categories = jpaCategoryRepository.findByParent_Name(savedChild.getParent()
                .getName());

        // then
        assertThat(categories.size()).isEqualTo(1);
    }
}

