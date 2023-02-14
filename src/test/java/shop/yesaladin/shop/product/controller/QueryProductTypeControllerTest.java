package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.dto.ProductTypeResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductTypeService;

@AutoConfigureRestDocs
@WebMvcTest(QueryProductTypeController.class)
class QueryProductTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryProductTypeService service;

    private final List<ProductTypeResponseDto> productTypes = List.of(
            new ProductTypeResponseDto(1, ProductTypeCode.NONE.toString(), ProductTypeCode.NONE.getKoName()),
            new ProductTypeResponseDto(2, ProductTypeCode.DISCOUNTS.toString(), ProductTypeCode.DISCOUNTS.getKoName())
    );

    @WithMockUser
    @Test
    @DisplayName("상품 유형 전체 조회 성공")
    void getProductTypes() throws Exception {
        // given
        Mockito.when(service.findAll()).thenReturn(productTypes);

        // when
        ResultActions result = mockMvc.perform(get("/v1/product-types")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.[1].id", equalTo(2)))
                .andExpect(jsonPath("$.data.[0].type", equalTo(ProductTypeCode.NONE.toString())))
                .andExpect(jsonPath("$.data.[1].type", equalTo(ProductTypeCode.DISCOUNTS.toString())));

        verify(service, times(1)).findAll();

        // docs
        result.andDo(document(
                "find-all-product-type",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("상품 유형 아이디"),
                        fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("상품 유형 이름"),
                        fieldWithPath("data.[].koName").type(JsonFieldType.STRING).description("상품 유형 한국 이름"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }
}
