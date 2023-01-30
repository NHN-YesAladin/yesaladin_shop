package shop.yesaladin.shop.publish.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.publish.domain.repository.SearchPublisherRepository;
import shop.yesaladin.shop.publish.dto.SearchPublisherRequestDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto.SearchedPublisherDto;

class SearchPublisherServiceImplTest {

    private SearchPublisherServiceImpl service;
    private SearchPublisherRepository searchPublisherRepository;

    @BeforeEach
    void setUp() {
        searchPublisherRepository = Mockito.mock(SearchPublisherRepository.class);
        service = new SearchPublisherServiceImpl(searchPublisherRepository);
    }

    @Test
    @DisplayName("이름으로 검색 서비스 테스트")
    void testSearchPublisherByName() {
        //given
        SearchPublisherResponseDto dummy = new SearchPublisherResponseDto(
                1L,
                List.of(new SearchedPublisherDto(1L, "publisher"))
        );
        Mockito.when(searchPublisherRepository.searchPublisherByName(any(), anyInt(), anyInt()))
                .thenReturn(dummy);

        //when
        SearchPublisherResponseDto result = service.searchPublisherByName(new SearchPublisherRequestDto(
                any(),
                anyInt(),
                anyInt()
        ));

        //then
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.getSearchedPublisherDtoList()).hasSize(1);
        assertThat(result.getSearchedPublisherDtoList()
                .get(0)
                .getId()).isEqualTo(dummy.getSearchedPublisherDtoList().get(0).getId());
        assertThat(result.getSearchedPublisherDtoList()
                .get(0)
                .getName()).isEqualTo(dummy.getSearchedPublisherDtoList().get(0).getName());
    }
}