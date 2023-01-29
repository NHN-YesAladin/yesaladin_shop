package shop.yesaladin.shop.tag.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@AutoConfigureRestDocs
@WebMvcTest(SearchTagController.class)
class SearchTagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchTagService searchTagService;
    private static final String NAME = "스프링";
    private TagsResponseDto dummy;

    @BeforeEach
    void setUp() {
        dummy = new TagsResponseDto(1L, NAME);
    }

    @Test
    @DisplayName("이름으로 검색 테스트")
    void testSearchByName() throws Exception {
        //given
        Mockito.when(searchTagService.searchTagByName(NAME)).thenReturn(List.of(dummy));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags").param("name", NAME)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].name", equalTo(NAME)))
                .andDo(print());

    }
}