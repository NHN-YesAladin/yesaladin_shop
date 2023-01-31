package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto.SearchedTagDto;

class SearchTagServiceImplTest {

    private SearchTagRepository searchTagRepository;
    private SearchTagServiceImpl service;
    private static final Long ID = 1L;
    private static final String NAME = "스프링";

    @BeforeEach
    void setUp() {
        searchTagRepository = Mockito.mock(SearchTagRepository.class);
        service = new SearchTagServiceImpl(searchTagRepository);
    }

    @Test
    @DisplayName("이름으로 검색 테스트")
    void testSearchTagByName() {
        //given
        SearchedTagDto dummy = new SearchedTagDto(ID, NAME);
        Mockito.when(searchTagRepository.searchTagByName(NAME, 0, 1))
                .thenReturn(SearchedTagResponseDto.builder()
                        .count(1L)
                        .searchedTagDtoList(List.of(dummy))
                        .build());
        SearchTagRequestDto requestDto = new SearchTagRequestDto(NAME, 0, 1);

        //when
        SearchedTagResponseDto results = service.searchTagByName(requestDto);

        //then
        assertThat(results.getCount()).isEqualTo(1);
        assertThat(results.getSearchedTagDtoList()).hasSize(1);
        assertThat(results.getSearchedTagDtoList().get(0).getId()).isEqualTo(1);
        assertThat(results.getSearchedTagDtoList().get(0).getName()).isEqualTo(NAME);
    }
}