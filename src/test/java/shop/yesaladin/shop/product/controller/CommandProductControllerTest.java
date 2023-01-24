package shop.yesaladin.shop.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.dummy.DummyProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;


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

    @Test
    @DisplayName("상품 등록 성공")
    void registerProduct() throws Exception {
        // given
        Mockito.when(service.create(any())).thenReturn(productOnlyIdDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(productCreateDto)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", equalTo(1)));

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
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER).description("개별할인율"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN).description("개별 할인 적용여부"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER).description("포인트 적립율"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN).description("포인트 적립여부"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 가능여부"),
                        fieldWithPath("isSale").type(JsonFieldType.BOOLEAN).description("상품 판매여부"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER).description("노출우선순위"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING).description("썸네일 파일 업로드 시간"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING).description("E-Book 파일 URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING).description("E-Book 파일 업로드 시간"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING).description("상품 유형"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING).description("상품 적립 방식"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 상품 아이디")
                )
        ));
    }

    @Test
    @DisplayName("상품 수정 성공")
    void updateProduct() throws Exception {
        // given
        Mockito.when(service.update(any(), any())).thenReturn(productOnlyIdDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productId}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(productUpdateDto)));

        // then
        result.andDo(print())
                .andExpect(status().isOk());

        // docs
        result.andDo(document(
                "update-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("publisherId").type(JsonFieldType.NUMBER).description("출판사"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER).description("개별할인율"),
                        fieldWithPath("isSeparatelyDiscount").type(JsonFieldType.BOOLEAN).description("개별 할인 적용여부"),
                        fieldWithPath("givenPointRate").type(JsonFieldType.NUMBER).description("포인트 적립율"),
                        fieldWithPath("isGivenPoint").type(JsonFieldType.BOOLEAN).description("포인트 적립여부"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 가능여부"),
                        fieldWithPath("isSale").type(JsonFieldType.BOOLEAN).description("상품 판매여부"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("preferentialShowRanking").type(JsonFieldType.NUMBER).description("노출우선순위"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("thumbnailFileUploadDateTime").type(JsonFieldType.STRING).description("썸네일 파일 업로드 시간"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING).description("E-Book 파일 URL"),
                        fieldWithPath("ebookFileUploadDateTime").type(JsonFieldType.STRING).description("E-Book 파일 업로드 시간"),
                        fieldWithPath("productTypeCode").type(JsonFieldType.STRING).description("상품 유형"),
                        fieldWithPath("productSavingMethodCode").type(JsonFieldType.STRING).description("상품 적립 방식"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그"),
                        fieldWithPath("isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제 품절 여부")
                )
        ));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productId}", ID));

        // then
        result.andDo(print()).andExpect(status().isOk());

        // docs
        result.andDo(document(
                "delete-product",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("삭제할 상품의 아이디"))
        ));
    }
}
