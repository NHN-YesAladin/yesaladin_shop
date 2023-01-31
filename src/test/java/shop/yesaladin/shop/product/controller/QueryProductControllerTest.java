package shop.yesaladin.shop.product.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductDetailResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

@AutoConfigureRestDocs
@WebMvcTest(QueryProductController.class)
class QueryProductControllerTest {
    private final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryProductService service;

    @Test
    @DisplayName("상품 상세 조회 성공")
    void findProductById() throws Exception {
        // given
        ProductDetailResponseDto responseDto = DummyProductDetailResponseDto.dummy();

        Mockito.when(service.findById(anyLong())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/{productId}", ID)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findById(ID);

        // docs
        result.andDo(document(
                "find-detail-product",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("조회할 상품의 아이디")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("ebookFileUrl").type(JsonFieldType.STRING).description("E-Book 파일 URL"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("pointPrice").type(JsonFieldType.NUMBER).description("포인트 적립금"),
                        fieldWithPath("pointRate").type(JsonFieldType.NUMBER).description("포인트 적립율"),
                        fieldWithPath("publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("ISBN"),
                        fieldWithPath("subscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 가능여부"),
                        fieldWithPath("issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("forcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제품절여부"),
                        fieldWithPath("sale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("deleted").type(JsonFieldType.BOOLEAN).description("삭제여부")
                )
        ));
    }

    @Test
    @DisplayName("관리자용 상품 페이징 조회 성공")
    void getProductsForManager() throws Exception {
        // given
        List<ProductsResponseDto> products = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            products.add(DummyProductsResponseDto.dummy(i));
        }
        for (long i = 11L; i <= 15L; i++) {
            products.add(DummyProductsResponseDto.deletedDummy(i));
        }

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );
        Mockito.when(service.findAllForManager(any(), any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findAllForManager(PageRequest.of(0, 5), null);

        // docs
        result.andDo(document(
                "find-all-for-manager-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("dataList.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("dataList.[].authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("dataList.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                        fieldWithPath("dataList.[].publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("dataList.[].discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("dataList.[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("dataList.[].isSale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제품절여부"),
                        fieldWithPath("dataList.[].isShown").type(JsonFieldType.BOOLEAN).description("노출여부"),
                        fieldWithPath("dataList.[].isDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("dataList.[].tags").type(JsonFieldType.ARRAY).description("태그"),
                        fieldWithPath("dataList.[].ebookFileUrl").description("E-book 파일 URL")
                        )
        ));
    }

    @Test
    @DisplayName("모든 사용자용 상품 페이징 조회 성공")
    void getProducts() throws Exception {
        // given
        List<ProductsResponseDto> products = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            products.add(DummyProductsResponseDto.dummy(i));
        }
        for (long i = 11L; i <= 15L; i++) {
            products.add(DummyProductsResponseDto.deletedDummy(i));
        }

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );
        Mockito.when(service.findAll(any(), any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findAll(PageRequest.of(0, 5), null);

        // docs
        result.andDo(document(
                "find-all-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("dataList.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("dataList.[].authors").type(JsonFieldType.ARRAY).description("작가"),
                        fieldWithPath("dataList.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                        fieldWithPath("dataList.[].publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("dataList.[].discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("dataList.[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("dataList.[].isSale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제품절여부"),
                        fieldWithPath("dataList.[].isShown").type(JsonFieldType.BOOLEAN).description("노출여부"),
                        fieldWithPath("dataList.[].isDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("dataList.[].tags").type(JsonFieldType.ARRAY).description("태그"),
                        fieldWithPath("dataList.[].ebookFileUrl").description("E-book 파일 URL")

                )
        ));
    }
}