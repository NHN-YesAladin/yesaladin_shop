package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;

@AutoConfigureRestDocs
@WebMvcTest(ElasticCommandProductController.class)
class ElasticCommandProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ElasticCommandProductService elasticCommandProductService;

    @WithMockUser
    @Test
    @DisplayName("상품 수정 성공")
    void update() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.update(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(put("/v1/search/products/{id}", 1)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        resultActions.andDo(
                document("elasticsearch-product-info-update",
                getDocumentRequest(),
                getDocumentResponse(),
                        pathParameters(parameterWithName("id").description("수정할 상품의 id")),
                requestParameters(
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 상품의 id"))
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("is-sale 수정 성공")
    void changeIsSale() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.changeIsSale(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products/is-sale/{id}", 1)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        resultActions.andDo(
                document("elasticsearch-product-is-Sale-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("id").description("판매여부를 수정할 상품의 id")),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("동작 성공 여부"),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("에러 메세지")
                                        .optional(),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("판매여부가 수정된 상품의 id"))
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("IsForcedOutOfStock 수정 성공")
    void changeIsForcedOutOfStock() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.changeIsForcedOutOfStock(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(get(
                "/v1/search/products/is-forced-out-of-stock/{id}",
                1
        )
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        resultActions.andDo(
                document("elasticsearch-product-IsForcedOutOfStock-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("id").description("강제품절 상태를 수정할 상품의 id")),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("동작 성공 여부"),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("에러 메세지")
                                        .optional(),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("강제품절 상태가 수정된 상품의 id"))
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("삭제 성공")
    void delete() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(
                "/v1/search/products/{id}",
                1
        ).with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)));

        verify(elasticCommandProductService, atLeastOnce()).delete(1L);

        resultActions.andDo(
                document("elasticsearch-product-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("id").description("삭제할 상품의 id")),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("삭제 상태"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("동작 성공 여부"),
                                fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                        .description("에러 메세지")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description(""))
                ));
    }
}