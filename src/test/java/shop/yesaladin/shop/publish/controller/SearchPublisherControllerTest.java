package shop.yesaladin.shop.publish.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.util.List;
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
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto.SearchedPublisherDto;
import shop.yesaladin.shop.publish.service.inter.SearchPublisherService;

@AutoConfigureRestDocs
@WebMvcTest(SearchPublisherController.class)
class SearchPublisherControllerTest {

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MIN = "-1";
    private static final String NAME = "name";
    private static final String OFFSET = "offset";
    private static final String SIZE = "size";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SearchPublisherService searchPublisherService;

    @WithMockUser
    @Test
    @DisplayName("????????? ????????? ???????????? ??? ?????? ConstraintViolationException")
    void testSearchPublisherByNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, MIN)
                .param(SIZE, ONE)
                .with(csrf()));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));

        resultActions.andDo(
                document(
                        "search-publisher-offset-min-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("????????? ???????????? ??????"),
                                parameterWithName("size").description("????????? ??????"),
                                parameterWithName("offset").description("????????? ??????"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("null")
                                        .optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("?????? ?????????")
                        )
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ????????? 1?????? ?????? ?????? ??? ?????? ConstraintViolationException")
    void testSearchPublisherByNameSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, ZERO)
                .with(csrf()));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));

        resultActions.andDo(
                document(
                        "search-publisher-size-min-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("????????? ???????????? ??????"),
                                parameterWithName("size").description("????????? ??????"),
                                parameterWithName("offset").description("????????? ??????"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("null")
                                        .optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("?????? ?????????")
                        )
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ????????? 20?????? ??? ?????? ??? ?????? ConstraintViolationException")
    void testSearchPublisherByNameSizeMoreThan20ThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/publishers")
                .accept(MediaType.APPLICATION_JSON)
                .param(NAME, "name")
                .param(OFFSET, ZERO)
                .param(SIZE, "21")
                .with(csrf()));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));

        resultActions.andDo(
                document(
                        "search-publisher-size-max-validation",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("????????? ???????????? ??????"),
                                parameterWithName("size").description("????????? ??????"),
                                parameterWithName("offset").description("????????? ??????"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("null")
                                        .optional(),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("?????? ?????????")
                        )
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ?????? ?????? ??????")
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
                .param(SIZE, ONE)
                .with(csrf()));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.searchedPublisherDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.searchedPublisherDtoList[0].name",
                        equalTo("publisher")
                ));

        resultActions.andDo(
                document(
                        "search-publisher-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("name").description("????????? ???????????? ??????"),
                                parameterWithName("size").description("????????? ??????"),
                                parameterWithName("offset").description("????????? ??????"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("null")
                                        .optional(),
                                fieldWithPath("data.count").type(JsonFieldType.NUMBER)
                                        .description("??? ??????"),
                                fieldWithPath("data.searchedPublisherDtoList.[].id").type(
                                        JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data.searchedPublisherDtoList.[].name").type(
                                        JsonFieldType.STRING).description("????????? ??????")
                        )
                ));
    }
}