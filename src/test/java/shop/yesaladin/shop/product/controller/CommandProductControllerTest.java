package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.dummy.DummyProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;

@AutoConfigureRestDocs
@WebMvcTest(CommandProductController.class)
class CommandProductControllerTest {

    private final String ISBN = "0000000000001";
    private final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandProductService service;

    private ProductCreateDto productCreateDto;
    private ProductUpdateDto productUpdateDto;
    private ProductOnlyIdDto productOnlyIdDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productCreateDto = DummyProductCreateDto.dummy(ISBN);
        productUpdateDto = DummyProductUpdateDto.dummy("??????");
        productOnlyIdDto = new ProductOnlyIdDto(ID);
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????")
    void registerProduct() throws Exception {
        // given
        Mockito.when(service.create(any())).thenReturn(productOnlyIdDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(productCreateDto)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(service, times(1)).create(any());

        // docs
        result.andDo(document(
                "register-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("ISBN"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("??????"),
                        fieldWithPath("publisherId").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER)
                                .description("???????????????"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ????????????"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("????????? ????????????"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN)
                                .description("?????? ????????????"),
                        fieldWithPath("isSale").type(JsonFieldType.BOOLEAN).description("?????? ????????????"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING)
                                .description("?????????"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER)
                                .description("??????????????????"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("????????? ?????? URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("????????? ?????? ????????? ??????"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING)
                                .description("E-Book ?????? URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("E-Book ?????? ????????? ??????"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("??????"),
                        fieldWithPath("categories").type(JsonFieldType.ARRAY).description("????????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? ?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????")
    void updateProduct() throws Exception {
        // given
        Mockito.when(service.update(any(), any())).thenReturn(productOnlyIdDto);

        // when
        ResultActions result = mockMvc.perform(put("/v1/products/{productId}", ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(productUpdateDto)));

        // then
        result.andDo(print())
                .andExpect(status().isOk());

        verify(service, times(1)).update(anyLong(), any());

        // docs
        result.andDo(document(
                "update-product",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("?????? ?????????")),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("??????"),
                        fieldWithPath("publisherId").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER)
                                .description("???????????????"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ????????????"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("????????? ????????????"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN)
                                .description("?????? ????????????"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING)
                                .description("?????????"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER)
                                .description("??????????????????"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("????????? ?????? URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("????????? ?????? ????????? ??????"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING)
                                .description("E-Book ?????? URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("E-Book ?????? ????????? ??????"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("??????"),
                        fieldWithPath("categories").type(JsonFieldType.ARRAY).description("????????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? ?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????")
    void deleteProduct() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productId}", ID).with(csrf()));

        // then
        result.andDo(print()).andExpect(status().isOk());

        verify(service, times(1)).softDelete(ID);

        // docs
        result.andDo(document(
                "delete-product",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("????????? ????????? ?????????"))
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ???????????? ?????? ??????")
    void changeProductIsSale() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productId}/is-sale", ID).with(
                csrf()));

        // then
        result.andDo(print()).andExpect(status().isOk());

        verify(service, times(1)).changeIsSale(ID);

        // docs
        result.andDo(document(
                "change-product-is-sale",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("??????????????? ????????? ????????? ?????????"))
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????????????????? ?????? ??????")
    void changeProductIsForcedOutOfStock() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post(
                "/v1/products/{productId}/is-forced-out-of-stock",
                ID
        ).with(csrf()));

        // then
        result.andDo(print()).andExpect(status().isOk());

        verify(service, times(1)).changeIsForcedOutOfStock(ID);

        // docs
        result.andDo(document(
                "change-product-is-forced-out-of-stock",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("????????????????????? ????????? ????????? ?????????"))
        ));
    }
}
