package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

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
        TagsResponseDto responseDto = new TagsResponseDto(ID, NAME);
        Mockito.when(searchTagRepository.searchTagByName(NAME)).thenReturn(List.of(responseDto));

        //when
        List<TagsResponseDto> results = service.searchTagByName(NAME);

        //then
        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(1);
        assertThat(results.get(0).getName()).isEqualTo(NAME);
    }
}