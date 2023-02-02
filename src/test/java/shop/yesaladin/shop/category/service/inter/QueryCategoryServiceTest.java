package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.service.impl.QueryCategoryServiceImpl;


class QueryCategoryServiceTest {

    private QueryCategoryRepository queryCategoryRepository;
    private QueryCategoryService queryCategoryService;

    @BeforeEach
    void setUp() {
        queryCategoryRepository = Mockito.mock(QueryCategoryRepository.class);

        queryCategoryService = new QueryCategoryServiceImpl(queryCategoryRepository);
    }

    @Test
    void findCategoriesByParentId() {
        //given
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        List<Category> list = new ArrayList<>();
        Category parent = CategoryDummy.dummyParent(1000L);
        for (int i = 0; i < 5; i++) {
            Category category = CategoryDummy.dummyChild((long) i, parent);
            list.add(category);
        }

        int size = 3;
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        Page<Category> categoryPage = new PageImpl<>(
                list.subList(start, end),
                pageRequest,
                list.size()
        );

        when(queryCategoryRepository.findCategoriesByParentId(
                any(),
                any()
        )).thenReturn(categoryPage);

        //when
        Page<CategoryResponseDto> categoryResponseDtoPage = queryCategoryService.findCategoriesByParentId(
                pageRequest,
                parent.getId()
        );

        //then
        assertThat(categoryResponseDtoPage.getPageable()).isEqualTo(pageRequest);
        assertThat(categoryResponseDtoPage.getContent()).hasSameSizeAs(categoryPage.getContent());

        verify(
                queryCategoryRepository,
                times(1)
        ).findCategoriesByParentId(pageableArgumentCaptor.capture(), longArgumentCaptor.capture());

        assertThat(pageableArgumentCaptor.getValue()
                .getOffset()).isEqualTo(pageRequest.getOffset());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(parent.getId());
    }


    @Test
    void findCategoryById() {
        //given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Long id = 1L;

        Category category = CategoryDummy.dummyParent(id);
        when(queryCategoryRepository.findById(any())).thenReturn(Optional.of(category));

        //when
        CategoryResponseDto responseDto = queryCategoryService.findCategoryById(id);

        //then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(category.getName());

        verify(queryCategoryRepository, times(1)).findById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);
    }


    @Test
    void findParentCategories() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .build();
            categories.add(category);
        }
        Collections.reverse(categories);
        when(queryCategoryRepository.findCategories(any(), eq(Category.DEPTH_PARENT))).thenReturn(
                categories);

        // when
        List<CategoryResponseDto> responseDtos = queryCategoryService.findParentCategories();

        // then
        assertThat(responseDtos.get(0).getId()).isEqualTo(categories.get(0).getId());
        assertThat(responseDtos.get(0).getName()).isEqualTo(categories.get(0).getName());
        assertThat(responseDtos.get(0).getOrder()).isEqualTo(categories.get(0).getOrder());
        assertThat(responseDtos.get(0).getParentId()).isNull();

        verify(queryCategoryRepository, times(1)).findCategories(
                longArgumentCaptor.capture(),
                eq(Category.DEPTH_PARENT)
        );
        assertThat(longArgumentCaptor.getValue()).isNull();
    }

    @Test
    void findChildCategoriesByParentId() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        List<Category> categories = new ArrayList<>();
        Category parent = CategoryDummy.dummyParent();
        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_CHILD)
                    .parent(parent)
                    .isShown(true)
                    .build();
            categories.add(category);
        }
        Collections.reverse(categories);
        when(queryCategoryRepository.findCategories(any(), any())).thenReturn(categories);

        // when
        List<CategoryResponseDto> responseDtos = queryCategoryService.findChildCategoriesByParentId(
                parent.getId());

        // then
        assertThat(responseDtos.get(0).getId()).isEqualTo(categories.get(0).getId());
        assertThat(responseDtos.get(0).getName()).isEqualTo(categories.get(0).getName());
        assertThat(responseDtos.get(0).getOrder()).isEqualTo(categories.get(0).getOrder());
        assertThat(responseDtos.get(0).getParentId()).isEqualTo(categories.get(0)
                .getParent()
                .getId());
        assertThat(responseDtos.get(0).getParentName()).isEqualTo(categories.get(0)
                .getParent()
                .getName());

        verify(queryCategoryRepository, times(1)).findCategories(
                longArgumentCaptor.capture(),
                integerArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(parent.getId());
        assertThat(integerArgumentCaptor.getValue()).isEqualTo(Category.DEPTH_CHILD);
    }

}
