package shop.yesaladin.shop.tag.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto.SearchedTagDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(SearchTagController.class)
class SearchTagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchTagService searchTagService;
    private static final String NAME = "스프링";
    private SearchedTagResponseDto dummyResponseDto;

    @BeforeEach
    void setUp() {
        dummyResponseDto = new SearchedTagResponseDto(1L, List.of(new SearchedTagDto(1L, NAME)));
    }

    @Test
    @DisplayName("페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchByNameOffsetLessThanZeroThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "-1")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("사이즈 갯수가 0보다 작을 경우 ConstraintViolationException")
    void testSearchByNameSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "0")
                .param("size", "0")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("이름으로 검색 성공")
    void testSearchByName() throws Exception {
        //given

        Mockito.when(searchTagService.searchTagByName(any()))
                .thenReturn(ResponseDto.<SearchedTagResponseDto>builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .data(dummyResponseDto)
                        .build().getData());

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedTagDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedTagDtoList[0].name", equalTo(NAME)))
                .andDo(print());
    }
}