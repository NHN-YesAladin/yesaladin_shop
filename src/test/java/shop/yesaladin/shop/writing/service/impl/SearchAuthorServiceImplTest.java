package shop.yesaladin.shop.writing.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.writing.domain.repository.SearchAuthorRepository;
import shop.yesaladin.shop.writing.dto.SearchAuthorRequestDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto.SearchedAuthorDto;

class SearchAuthorServiceImplTest {

    private SearchAuthorServiceImpl searchAuthorService;
    private SearchAuthorRepository searchAuthorRepository;

    @BeforeEach
    void setUp() {
        searchAuthorRepository = Mockito.mock(SearchAuthorRepository.class);
        searchAuthorService = new SearchAuthorServiceImpl(searchAuthorRepository);
    }

    @Test
    @DisplayName("저자 이름으로 검색 테스트")
    void testSearchAuthorByName() {
        Mockito.when(searchAuthorRepository.searchAuthorByName(any(), anyInt(), anyInt()))
                .thenReturn(
                        SearchedAuthorResponseDto.builder()
                                .count(1L)
                                .searchedAuthorDtoList(List.of(new SearchedAuthorDto(
                                        1L,
                                        "author",
                                        "loginId"
                                )))
                                .build());
        SearchedAuthorResponseDto result = searchAuthorService.searchAuthorByName(new SearchAuthorRequestDto(
                "name",
                0,
                1
        ));

        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getSearchedAuthorDtoList()).hasSize(1);
        assertThat(result.getSearchedAuthorDtoList().get(0).getId()).isEqualTo(1);
        assertThat(result.getSearchedAuthorDtoList().get(0).getName()).isEqualTo("author");
        assertThat(result.getSearchedAuthorDtoList().get(0).getLoginId()).isEqualTo("loginId");
    }
}