package shop.yesaladin.shop.writing.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto.SearchedCategoryDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto.SearchedAuthorDto;
import shop.yesaladin.shop.writing.service.inter.SearchAuthorService;

@WebMvcTest(SearchAuthorController.class)
class SearchAuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchAuthorService service;
    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MIN = "-1";
    private static final String NAME = "name";
    private static final String OFFSET = "offset";
    private static final String SIZE = "size";

    @Test
    @DisplayName("페이지 위치가 미이너스 일 경우 ConstraintViolationException")
    void testSearchAuthorByNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/authors")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, MIN)
                .param(SIZE, ONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("페이지 위치가 미이너스 일 경우 ConstraintViolationException")
    void testSearchAuthorByNameSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/authors")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("페이지 위치가 미이너스 일 경우 ConstraintViolationException")
    void testSearchAuthorByNameSizeMoreThan20ThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/authors")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, "21"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("저자 이름 검색 성공")
    void testSearchCategoryByNameSuccess() throws Exception {
        //given

        Mockito.when(service.searchAuthorByName(any()))
                .thenReturn(new SearchedAuthorResponseDto(
                        1L,
                        List.of(new SearchedAuthorDto(1L, "author"))
                ));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/authors")
                .param(NAME, "author")
                .param(OFFSET, ZERO)
                .param(SIZE, ONE)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(1)))
                .andExpect(jsonPath("$.searchedAuthorDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.searchedAuthorDtoList[0].name", equalTo("author")));
    }
}