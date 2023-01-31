package shop.yesaladin.shop.publish.controller;

import static org.hamcrest.Matchers.equalTo;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto.SearchedPublisherDto;
import shop.yesaladin.shop.publish.service.inter.SearchPublisherService;

@WebMvcTest(SearchPublisherController.class)
class SearchPublisherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchPublisherService searchPublisherService;

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MIN = "-1";
    private static final String NAME = "name";
    private static final String OFFSET = "offset";
    private static final String SIZE = "size";

    @Test
    @DisplayName("페이지 위치가 미이너스 일 경우 ConstraintViolationException")
    void testSearchPublisherByNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, MIN)
                .param(SIZE, ONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("데이터 갯수가 1보다 작을 경우 일 경우 ConstraintViolationException")
    void testSearchPublisherByNameSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("데이터 갯수가 20보다 클 경우 일 경우 ConstraintViolationException")
    void testSearchPublisherByNameSizeMoreThan20ThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, "21"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("출판사 이름 검색 성공")
    void testSearchPublisherNameSuccess() throws Exception {
        //given
        SearchPublisherResponseDto dummy = new SearchPublisherResponseDto(
                1L,
                List.of(new SearchedPublisherDto(1L, "publisher"))
        );
        Mockito.when(searchPublisherService.searchPublisherByName(any()))
                .thenReturn(ResponseDto.<SearchPublisherResponseDto>builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .data(dummy)
                        .build()
                        .getData());
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedPublisherDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedPublisherDtoList[0].name", equalTo("publisher")));
    }
}