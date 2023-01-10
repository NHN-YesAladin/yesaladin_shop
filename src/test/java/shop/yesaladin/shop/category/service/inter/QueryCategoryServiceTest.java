package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.impl.QueryCategoryServiceImpl;


@Slf4j
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

        int size = 3;
        PageRequest pageRequest = PageRequest.of(0, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        Page<Category> categoryPage = new PageImpl<>(
                list.subList(start, end),
                pageRequest,
                list.size()
        );

        given(queryCategoryRepository.findAll(any())).willReturn(categoryPage);

        //when
        Page<CategoryResponseDto> categoryResponseDtoPage = queryCategoryService.findCategories(pageRequest);
        log.info("categoryPage.getTotalElements() : {}",categoryPage.getTotalElements());
        log.info("categoryResponseDtoPage.getTotalElements() : {}",categoryResponseDtoPage.getTotalElements());
        //then
        assertThat(categoryResponseDtoPage.getPageable()).isEqualTo(pageRequest);
        assertThat(categoryResponseDtoPage.getContent().size()).isEqualTo(categoryPage.getContent().size());

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
        CategoryResponseDto responseDto = queryCategoryService.findCategoryById(id);

        //then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(name);

        verify(queryCategoryRepository, times(1)).findById(any());
    }
}
