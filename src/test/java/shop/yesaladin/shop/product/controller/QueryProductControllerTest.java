package shop.yesaladin.shop.product.controller;

import jakarta.json.Json;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;
import shop.yesaladin.shop.product.dummy.DummyProductDetailResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

    @WithMockUser
    @Test
    @DisplayName("상품 ISBN으로 제목 조회 성공")
    void findTitleByIsbn_success() throws Exception {
        // given
        String isbn = "0000000000001";
        ProductOnlyTitleDto productOnlyTitleDto = new ProductOnlyTitleDto("제목");

        Mockito.when(service.findTitleByIsbn(isbn)).thenReturn(productOnlyTitleDto);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/info/{isbn}", isbn)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)));

        verify(service, times(1)).findTitleByIsbn(isbn);

        // docs
        result.andDo(document(
                "find-title-by-isbn",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("isbn").description("ISBN")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목").optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL).description("에러 메세지 NULL")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 ISBN으로 제목 조회 실패_ISBN로 조회되는 상품이 없는 경우 예외 발생")
    void findTitleByIsbn_notExistISBN_throwProductNotFoundException() throws Exception {
        // given
        String isbn = "0000000000001";
        Mockito.when(service.findTitleByIsbn(isbn))
                .thenThrow(new ClientException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found with isbn : " + isbn));

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/info/{isbn}", isbn)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())));

        verify(service, times(1)).findTitleByIsbn(isbn);

        // docs
        result.andDo(document(
                "find-title-by-isbn-throw-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("isbn").description("ISBN")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("NULL"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지")
                )
        ));
    }

    // TODO: ResponseDto 수정

    @WithMockUser
    @Test
    @DisplayName("상품 상세 조회 성공")
    void findDetailProductById() throws Exception {
        // given
        ProductDetailResponseDto responseDto = DummyProductDetailResponseDto.dummy();

        Mockito.when(service.findDetailProductById(anyLong())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/{productId}", ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findDetailProductById(ID);

        // docs
        result.andDo(document(
                "find-detail-product",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("productId").description("조회할 상품의 아이디")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.isEbook").type(JsonFieldType.BOOLEAN).description("E-Book 여부"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.authors.[].id").type(JsonFieldType.NUMBER).description("작가 아이디"),
                        fieldWithPath("data.authors.[].name").type(JsonFieldType.STRING).description("작가 이름"),
                        fieldWithPath("data.authors.[].loginId").type(JsonFieldType.STRING).description("작가 로그인 아이디"),
                        fieldWithPath("data.publisher.id").type(JsonFieldType.NUMBER).description("출판사 아이디"),
                        fieldWithPath("data.publisher.name").type(JsonFieldType.STRING).description("출판사 이름"),
                        fieldWithPath("data.thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("data.actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("data.sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("data.discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("data.pointPrice").type(JsonFieldType.NUMBER).description("포인트 적립금"),
                        fieldWithPath("data.pointRate").type(JsonFieldType.NUMBER).description("포인트 적립율"),
                        fieldWithPath("data.publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("ISBN"),
                        fieldWithPath("data.isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 가능여부"),
                        fieldWithPath("data.issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("data.contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("data.onSale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("data.tags.[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                        fieldWithPath("data.tags.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                        fieldWithPath("data.categories.[].id").type(JsonFieldType.NUMBER).description("연관된 카테고리 Id"),
                        fieldWithPath("data.categories.[].name").type(JsonFieldType.STRING).description("연관된 카테고리 이름"),
                        fieldWithPath("data.categories.[].isShown").type(JsonFieldType.BOOLEAN).description("연관된 카테고리 노출여부"),
                        fieldWithPath("data.categories.[].order").type(JsonFieldType.NUMBER).description("연관된 카테고리 노출순서"),
                        fieldWithPath("data.categories.[].parentId").type(JsonFieldType.NUMBER).description("연관된 카테고리의 부모카테고리 Id"),
                        fieldWithPath("data.categories.[].parentName").type(JsonFieldType.STRING).description("연관된 카테고리의 부모카테고리 이름 "),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("관리자용 상품 페이징 조회 성공")
    void getProductsForManager() throws Exception {
        // given
        List<ProductsResponseDto> products = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            products.add(DummyProductsResponseDto.dummy(i, true, false));
        }
        for (long i = 11L; i <= 15L; i++) {
            products.add(DummyProductsResponseDto.dummy(i, false, true));
        }

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );
        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findAllForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .with(csrf())
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
                "find-all-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER).description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING).description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING).description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER).description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING).description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN).description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN).description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("모든 사용자용 상품 페이징 조회 성공")
    void getProducts() throws Exception {
        // given
        List<ProductsResponseDto> products = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            products.add(DummyProductsResponseDto.dummy(i, true, false));
        }
        for (long i = 11L; i <= 15L; i++) {
            products.add(DummyProductsResponseDto.dummy(i, false, true));
        }

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );
        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findAll(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products")
                .with(csrf())
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
                        parameterWithName("page").description("페이지네이션 페이지 번호"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER).description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING).description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING).description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER).description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING).description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN).description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN).description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING).description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN).description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("장바구니 정보 조회 성공")
    void getCartProducts() throws Exception {
        // given
        String isbn = "0000000000001";
        Map<String, String> cart = new HashMap<>();
        cart.put("1", "100");

        List<ViewCartDto> response = new ArrayList<>();
        response.add(ViewCartDto.builder()
                .id(1L)
                .quantity(100)
                .isbn(isbn)
                .thumbnailFileUrl("thumbnailFileUrl")
                .title("제목")
                .actualPrice(1000L)
                .sellingPrice(900L)
                .discountRate(10)
                .pointPrice(10)
                .isOutOfStack(false)
                .isSale(true)
                .isDeleted(false)
                .isEbook(false)
                .isSubscribeProduct(false)
                .build());

        Mockito.when(service.getCartProduct(cart)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .param("1", "100"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())));

        verify(service, times(1)).getCartProduct(cart);

        // docs
        result.andDo(document(
                "get-cart-products",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(

                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.[].quantity").type(JsonFieldType.NUMBER).description("상품 개수"),
                        fieldWithPath("data.[].isbn").type(JsonFieldType.STRING).description("ISBN"),
                        fieldWithPath("data.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("썸네일 파일 URL"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.[].actualPrice").type(JsonFieldType.NUMBER).description("정가"),
                        fieldWithPath("data.[].sellingPrice").type(JsonFieldType.NUMBER).description("판매가"),
                        fieldWithPath("data.[].discountRate").type(JsonFieldType.NUMBER).description("할인율"),
                        fieldWithPath("data.[].pointPrice").type(JsonFieldType.NUMBER).description("포인트 적립금"),
                        fieldWithPath("data.[].isOutOfStack").type(JsonFieldType.BOOLEAN).description("품절 여부"),
                        fieldWithPath("data.[].isSale").type(JsonFieldType.BOOLEAN).description("판매 여부"),
                        fieldWithPath("data.[].isDeleted").type(JsonFieldType.BOOLEAN).description("삭제 여부"),
                        fieldWithPath("data.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-Book 여부"),
                        fieldWithPath("data.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN).description("구독 가능여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL).description("에러 메세지 NULL")
                )
        ));
    }
}
