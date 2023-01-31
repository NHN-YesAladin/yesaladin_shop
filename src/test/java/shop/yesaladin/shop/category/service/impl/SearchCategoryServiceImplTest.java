package shop.yesaladin.shop.category.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.category.domain.repository.SearchCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryRequestDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto.SearchedCategoryDto;

class SearchCategoryServiceImplTest {

    private SearchCategoryRepository searchCategoryRepository;
    private SearchCategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        searchCategoryRepository = Mockito.mock(SearchCategoryRepository.class);
        service = new SearchCategoryServiceImpl(searchCategoryRepository);
    }

    @Test
    @DisplayName("이름으로 검색 테스트")
    void testSearchCategoryByName() {
        //given
        CategoryResponseDto dto = CategoryResponseDto.builder()
                .id(1L)
                .name("name")
                .parentName("parentName")
                .isShown(true)
                .build();
        Mockito.when(searchCategoryRepository.searchCategoryByName(any(), anyInt(), anyInt()))
                .thenReturn(SearchCategoryResponseDto.builder()
                        .count(1L)
                        .searchedCategoryDtoList(List.of(new SearchedCategoryDto(1L,
                                "name",
                                "parentName"
                        )))
                        .build());

        //when
        SearchCategoryResponseDto result = service.searchCategoryByName(new SearchCategoryRequestDto(
                "name",
                1,
                1
        ));

        //then
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.getSearchedCategoryDtoList()).hasSize(1);
        assertThat(result.getSearchedCategoryDtoList().get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.getSearchedCategoryDtoList().get(0).getName()).isEqualTo(dto.getName());
    }
}