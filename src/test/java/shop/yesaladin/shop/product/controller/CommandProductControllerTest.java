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
        productUpdateDto = DummyProductUpdateDto.dummy("제목");
        productOnlyIdDto = new ProductOnlyIdDto(ID);
    }

    @WithMockUser
    @Test
    @DisplayName("상품 등록 성공")
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
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("publisherId").type(JsonFieldType.NUMBER).description("출판사"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER)
                                .description("개별할인율"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN)
                                .description("개별 할인 적용여부"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER)
                                .description("포인트 적립율"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("포인트 적립여부"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN)
                                .description("구독 가능여부"),
                        fieldWithPath("isSale").type(JsonFieldType.BOOLEAN).description("상품 판매여부"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER)
                                .description("노출우선순위"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("썸네일 파일 업로드 시간"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING)
                                .description("E-Book 파일 URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("E-Book 파일 업로드 시간"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING)
                                .description("상품 유형"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING)
                                .description("상품 적립 방식"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                        fieldWithPath("categories").type(JsonFieldType.ARRAY).description("카테고리")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("생성된 상품 아이디"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 수정 성공")
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
                pathParameters(parameterWithName("productId").description("상품 아이디")),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("publisherId").type(JsonFieldType.NUMBER).description("출판사"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER)
                                .description("개별할인율"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN)
                                .description("개별 할인 적용여부"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER)
                                .description("포인트 적립율"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("포인트 적립여부"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN)
                                .description("구독 가능여부"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER)
                                .description("노출우선순위"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("썸네일 파일 업로드 시간"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING)
                                .description("E-Book 파일 URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING)
                                .description("E-Book 파일 업로드 시간"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING)
                                .description("상품 유형"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING)
                                .description("상품 적립 방식"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                        fieldWithPath("categories").type(JsonFieldType.ARRAY).description("카테고리")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("수정된 상품 아이디"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 삭제 성공")
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
                pathParameters(parameterWithName("productId").description("삭제할 상품의 아이디"))
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 판매여부 변경 성공")
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
                pathParameters(parameterWithName("productId").description("판매여부를 변경할 상품의 아이디"))
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 강제품절여부 변경 성공")
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
                pathParameters(parameterWithName("productId").description("강제품절여부를 변경할 상품의 아이디"))
        ));
    }
}
