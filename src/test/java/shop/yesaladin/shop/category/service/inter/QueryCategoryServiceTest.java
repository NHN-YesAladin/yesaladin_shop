package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
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
    void findCategories() {
        //given
        List<Category> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = Category.builder()
                    .name(i + "")
                    .order(null)
                    .isShown(true)
                    .parent(null)
                    .build();
            list.add(category);
        }

        int size = 2;
        PageRequest pageRequest = PageRequest.of(1, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        Page<Category> page = new PageImpl<>(
                list.subList(start, end),
                pageRequest,
                list.size()
        );

        given(queryCategoryRepository.findAll(any())).willReturn(page);

        //when
        Page<Category> categories = queryCategoryService.findCategories(pageRequest);

        //then
        assertThat(categories.getTotalElements()).isEqualTo(list.size());
        assertThat(categories.getContent().size()).isEqualTo(page.getContent().size());

        verify(queryCategoryRepository, times(1)).findAll(any());
    }

    @Test
    void findCategoryById() {
        //given
        Long id = 1L;
        String name = "서적";

        Category category = Category.builder()
                .id(id)
                .name(name)
                .order(null)
                .isShown(true)
                .parent(null)
                .build();
        given(queryCategoryRepository.findById(any())).willReturn(Optional.of(category));

        //when
        Category categoryById = queryCategoryService.findCategoryById(id);

        //then
        assertThat(categoryById.getId()).isEqualTo(id);
        assertThat(categoryById.getName()).isEqualTo(name);

        verify(queryCategoryRepository, times(1)).findById(any());
    }
}
