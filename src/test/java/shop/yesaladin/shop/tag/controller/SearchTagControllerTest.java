package shop.yesaladin.shop.tag.controller;


import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.util.List;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto.SearchedTagDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@AutoConfigureRestDocs
@WebMvcTest(SearchTagController.class)
class SearchTagControllerTest {

    private static final String NAME = "스프링";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchTagService searchTagService;
    private SearchedTagResponseDto dummyResponseDto;

    @BeforeEach
    void setUp() {
        dummyResponseDto = new SearchedTagResponseDto(1L, List.of(new SearchedTagDto(1L, NAME)));
    }

    @WithMockUser
    @Test
    @DisplayName("페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchByNameOffsetLessThanZeroThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "-1")
                .param("size", "1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));
        //docs
        resultActions.andDo(
                document(
                        "search-tag-offset-min-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("검색할 테그 이름"),
                                parameterWithName("size").description("데이터 갯수"),
                                parameterWithName("offset").description("페이지 위치"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메시지"))
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("사이즈 갯수가 0보다 작을 경우 ConstraintViolationException")
    void testSearchByNameSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "0")
                .param("size", "0")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));
        //docs
        resultActions.andDo(
                document(
                        "search-tag-size-m-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("검색할 테그 이름"),
                                parameterWithName("size").description("데이터 갯수"),
                                parameterWithName("offset").description("페이지 위치"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메시지"))
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("사이즈 갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchByNameSizeGreaterThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/tags")
                .param("name", NAME)
                .param("offset", "0")
                .param("size", "21")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));
        //docs
        resultActions.andDo(
                document(
                        "search-tag-size-max-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("검색할 테그 이름"),
                                parameterWithName("size").description("데이터 갯수"),
                                parameterWithName("offset").description("페이지 위치"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메시지"))
                ));
    }

    @WithMockUser
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
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedTagDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedTagDtoList[0].name", equalTo(NAME)))
                .andDo(print());

        //docs
        resultActions.andDo(
                document(
                        "search-tag-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("검색할 테그 이름"),
                                parameterWithName("size").description("데이터 갯수"),
                                parameterWithName("offset").description("페이지 위치"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태"),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("null").optional(),
                                fieldWithPath("data.count").type(JsonFieldType.NUMBER).description("총 갯수"),
                                fieldWithPath("data.searchedTagDtoList.[].id").type(JsonFieldType.NUMBER).description("태그 id"),
                                fieldWithPath("data.searchedTagDtoList.[].name").type(JsonFieldType.STRING).description("태그 이름"))
                ));
    }
}