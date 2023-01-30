package shop.yesaladin.shop.category.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto.SearchedCategoryDto;
import shop.yesaladin.shop.category.service.inter.SearchCategoryService;

@WebMvcTest(SearchCategoryController.class)
class SearchCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchCategoryService searchCategoryService;

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MIN = "-1";
    private static final String NAME = "name";
    private static final String OFFSET = "offset";
    private static final String SIZE = "size";

    @Test
    @DisplayName("페이지 위치가 미이너스 일 경우 ConstraintViolationException")
    void testSearchCategoryByNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/categories")
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
    void testSearchCategoryByNameSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/categories")
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
    void testSearchCategoryByNameSizeMoreThan20ThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/categories")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, "21"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 이름 검색 성공")
    void testSearchCategoryByNameSuccess() throws Exception {
        //given

        Mockito.when(searchCategoryService.searchCategoryByName(any())).thenReturn(
                SearchCategoryResponseDto.builder()
                        .count(1L)
                        .searchedCategoryDtoList(List.of(new SearchedCategoryDto(
                                1L,
                                "name",
                                "parentName"
                        ))).build());

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/categories")
                .param(NAME, "category")
                .param(OFFSET, ZERO)
                .param(SIZE, ONE)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(1)))
                .andExpect(jsonPath("$.searchedCategoryDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.searchedCategoryDtoList[0].name", equalTo("name")))
                .andExpect(jsonPath("$.searchedCategoryDtoList[0].parentName", equalTo("parentName")));
    }
}