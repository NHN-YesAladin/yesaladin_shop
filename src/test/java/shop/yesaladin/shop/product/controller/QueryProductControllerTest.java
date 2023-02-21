package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductRecentResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.RecentViewProductRequestDto;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;
import shop.yesaladin.shop.product.dummy.DummyProductDetailResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

@AutoConfigureRestDocs
@WebMvcTest(QueryProductController.class)
class QueryProductControllerTest {

    private final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                .description("제목")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL)
                                .description("에러 메세지 NULL")
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
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with isbn : " + isbn
                ));

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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("NULL"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 ISBN 중복 체크 성공")
    void existsByIsbn_success() throws Exception {
        // given
        String isbn = "0000000000001";
        Mockito.when(service.existsByIsbn(isbn)).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/check/{isbn}", isbn)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)));

        verify(service, times(1)).existsByIsbn(isbn);

        // docs
        result.andDo(document(
                "exists-by-isbn",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("isbn").description("ISBN")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                .description("ISBN 존재여부")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL)
                                .description("에러 메세지 NULL")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 ID로 수량 조회 성공")
    void findQuantityById_success() throws Exception {
        // given
        Long id = 1L;
        Long quantity = 100L;
        Mockito.when(service.findQuantityById(id)).thenReturn(quantity);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/quantity/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)));

        verify(service, times(1)).findQuantityById(id);

        // docs
        result.andDo(document(
                "find-quantity-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("id").description("ID")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("수량")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL)
                                .description("에러 메세지 NULL")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 ID로 수량 조회 실패_ID로 조회되는 상품이 없는 경우 예외 발생")
    void findQuantityById_notExistId_throwProductNotFoundException() throws Exception {
        // given
        Long id = 1L;
        Mockito.when(service.findQuantityById(id))
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with id : " + id
                ));

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/quantity/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())));

        verify(service, times(1)).findQuantityById(id);

        // docs
        result.andDo(document(
                "find-quantity-by-id-throw-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("id").description("ID")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("NULL"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-Book 여부"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.actualPrice").type(JsonFieldType.NUMBER)
                                .description("정가"),
                        fieldWithPath("data.sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.pointPrice").type(JsonFieldType.NUMBER)
                                .description("포인트 적립금"),
                        fieldWithPath("data.pointRate").type(JsonFieldType.NUMBER)
                                .description("포인트 적립율"),
                        fieldWithPath("data.publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("ISBN"),
                        fieldWithPath("data.isSubscriptionAvailable").type(JsonFieldType.BOOLEAN)
                                .description("구독 가능여부"),
                        fieldWithPath("data.issn").type(JsonFieldType.STRING).description("ISSN"),
                        fieldWithPath("data.contents").type(JsonFieldType.STRING).description("목차"),
                        fieldWithPath("data.description").type(JsonFieldType.STRING)
                                .description("설명"),
                        fieldWithPath("data.onSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.categories.[].id").type(JsonFieldType.NUMBER)
                                .description("연관된 카테고리 Id"),
                        fieldWithPath("data.categories.[].name").type(JsonFieldType.STRING)
                                .description("연관된 카테고리 이름"),
                        fieldWithPath("data.categories.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("연관된 카테고리 노출여부"),
                        fieldWithPath("data.categories.[].order").type(JsonFieldType.NUMBER)
                                .description("연관된 카테고리 노출순서"),
                        fieldWithPath("data.categories.[].parentId").type(JsonFieldType.NUMBER)
                                .description("연관된 카테고리의 부모카테고리 Id"),
                        fieldWithPath("data.categories.[].parentName").type(JsonFieldType.STRING)
                                .description("연관된 카테고리의 부모카테고리 이름 "),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("제목으로 검색")
    void getProductsByTitleForManager() throws Exception {
        List<ProductsResponseDto> products = List.of(DummyProductsResponseDto.dummy(
                1L,
                true,
                false
        ));

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 1),
                products.size()
        );

        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findByTitleForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .queryParam("title", products.get(0).getTitle())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        verify(service, atLeastOnce()).findByTitleForManager(any(), any());

        // docs
        result.andDo(document(
                "find-product-by-title",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("title").description("검색할 상품의 제목"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("내용으로 검색")
    void getProductsByContentForManager() throws Exception {
        List<ProductsResponseDto> products = List.of(DummyProductsResponseDto.dummy(
                1L,
                true,
                false
        ));

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 1),
                products.size()
        );

        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findByContentForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .queryParam("content", "content")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // docs
        result.andDo(document(
                "find-product-by-content",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("content").description("검색할 상품의 내용"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    void getProductsByPublisherForManager() throws Exception {
        List<ProductsResponseDto> products = List.of(DummyProductsResponseDto.dummy(
                1L,
                true,
                false
        ));

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 1),
                products.size()
        );

        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findByPublisherForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .queryParam("publisher", "publisher")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        verify(service, atLeastOnce()).findByPublisherForManager(any(), any());

        // docs
        result.andDo(document(
                "find-product-by-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("publisher").description("검색할 상품의 출판사"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    void getProductsByAuthorForManager() throws Exception {
        List<ProductsResponseDto> products = List.of(DummyProductsResponseDto.dummy(
                1L,
                true,
                false
        ));

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 1),
                products.size()
        );

        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findByAuthorForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .queryParam("author", "author")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        verify(service, atLeastOnce()).findByAuthorForManager(any(), any());

        // docs
        result.andDo(document(
                "find-product-by-author",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("author").description("검색할 상품의 저자"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("isbn으로 검색")
    void getProductsByISBNForManager() throws Exception {
        List<ProductsResponseDto> products = List.of(DummyProductsResponseDto.dummy(
                1L,
                true,
                false
        ));

        Page<ProductsResponseDto> page = new PageImpl<>(
                products,
                PageRequest.of(0, 1),
                products.size()
        );

        PaginatedResponseDto<ProductsResponseDto> paginated = PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
        Mockito.when(service.findByISBNForManager(any(), any())).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/manager")
                .queryParam("isbn", "isbn")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        verify(service, atLeastOnce()).findByISBNForManager(any(), any());

        // docs
        result.andDo(document(
                "find-product-by-isbn",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("isbn").description("검색할 상품의 ISBN"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors.[].id").type(JsonFieldType.NUMBER)
                                .description("작가 아이디"),
                        fieldWithPath("data.dataList.[].authors.[].name").type(JsonFieldType.STRING)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].authors.[].loginId").type(JsonFieldType.STRING)
                                .description("작가 로그인 아이디"),
                        fieldWithPath("data.dataList.[].publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("data.dataList.[].publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("수량"),
                        fieldWithPath("data.dataList.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매여부"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제품절여부"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("노출여부"),
                        fieldWithPath("data.dataList.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제여부"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.dataList.[].tags.[].id").type(JsonFieldType.NUMBER)
                                .description("태그 아이디"),
                        fieldWithPath("data.dataList.[].tags.[].name").type(JsonFieldType.STRING)
                                .description("태그 이름"),
                        fieldWithPath("data.dataList.[].ebookFileUrl").description("E-book 파일 URL"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독상품 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.[].quantity").type(JsonFieldType.NUMBER)
                                .description("상품 개수"),
                        fieldWithPath("data.[].isbn").type(JsonFieldType.STRING)
                                .description("ISBN"),
                        fieldWithPath("data.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.[].actualPrice").type(JsonFieldType.NUMBER)
                                .description("정가"),
                        fieldWithPath("data.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.[].discountRate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.[].pointPrice").type(JsonFieldType.NUMBER)
                                .description("포인트 적립금"),
                        fieldWithPath("data.[].isOutOfStack").type(JsonFieldType.BOOLEAN)
                                .description("품절 여부"),
                        fieldWithPath("data.[].isSale").type(JsonFieldType.BOOLEAN)
                                .description("판매 여부"),
                        fieldWithPath("data.[].isDeleted").type(JsonFieldType.BOOLEAN)
                                .description("삭제 여부"),
                        fieldWithPath("data.[].isEbook").type(JsonFieldType.BOOLEAN)
                                .description("E-Book 여부"),
                        fieldWithPath("data.[].isSubscribeProduct").type(JsonFieldType.BOOLEAN)
                                .description("구독 가능여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL)
                                .description("에러 메세지 NULL")

                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("연관 상품 등록을 위한 상품 검색 성공")
    void findProductRelationByTitle() throws Exception {
        //given
        String title = "title";
        List<RelationsResponseDto> lists = new ArrayList<>();
        RelationsResponseDto dto = new RelationsResponseDto(
                10L,
                "file",
                title,
                List.of("author"),
                "publisher",
                LocalDate.now().toString(),
                1000000000,
                100
        );

        lists.add(dto);

        Mockito.when(service.findProductRelationByTitle(0L, title, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(lists, PageRequest.of(0, 5), 5));

        ResultActions result = mockMvc.perform(get("/v1/products/{id}/relation", "0").queryParam(
                "title",
                title
        ).with(csrf()));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andDo(document(
                "search-product-by-title-for-relation",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("메인 상품 id")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY)
                                .description("작가 이름"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING)
                                .description("출간일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("썸네일 파일 URL"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("최신 상품 검색 성공")
    void findRecentProductByPublishedDate() throws Exception {
        //given
        ProductRecentResponseDto productRecentResponseDto = ProductRecentResponseDto.builder()
                .id(1L)
                .title("title")
                .thumbnailFileUrl("file")
                .publisher("publisher")
                .rate(10)
                .sellingPrice(10000L)
                .author(List.of("author"))
                .isForcedOutOfStock(false)
                .quantity(1000L)
                .build();
        Mockito.when(service.findRecentProductByPublishedDate(PageRequest.of(0, 10)))
                .thenReturn(List.of(productRecentResponseDto));

        //when
        ResultActions result = mockMvc.perform(get("/v1/products/recent/product").with(csrf()));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andDo(document(
                "find-recent-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공 야부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("상태"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                                .optional(),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                .description("상품의 id"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING)
                                .description("상품 제목"),
                        fieldWithPath("data.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("상품 썸네일 파일"),
                        fieldWithPath("data.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.[].rate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.[].author").type(JsonFieldType.ARRAY)
                                .description("저자명"),
                        fieldWithPath("data.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제 품절 상태"),
                        fieldWithPath("data.[].quantity").type(JsonFieldType.NUMBER)
                                .description("상품 재고량"),
                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING)
                                .description("상품 출판사")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("최근 본 상품 검색 성공")
    void findRecentViewProductById() throws Exception {
        //given
        ProductRecentResponseDto productRecentResponseDto = ProductRecentResponseDto.builder()
                .id(1L)
                .title("title")
                .thumbnailFileUrl("file")
                .publisher("publisher")
                .rate(10)
                .sellingPrice(10000L)
                .author(List.of("author"))
                .isForcedOutOfStock(false)
                .quantity(1000L)
                .build();
        Mockito.when(service.findRecentViewProductById(
                        List.of(1L),
                        List.of(1L),
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(
                        List.of(productRecentResponseDto),
                        PageRequest.of(0, 1),
                        1L
                ));
        String body = objectMapper.writeValueAsString(new RecentViewProductRequestDto(
                List.of(1L),
                List.of(1L)
        ));

        //when
        ResultActions result = mockMvc.perform(post("/v1/products/recentview/product").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andDo(document(
                "find-recent-view-product",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("totalIds").type(JsonFieldType.ARRAY)
                                .description("전체 최근 본 상품의 id 리스트"),
                        fieldWithPath("pageIds").type(JsonFieldType.ARRAY)
                                .description("현재 페이지의 최근 본 상품 id 리스트")
                ),
                requestParameters(
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공 야부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("상태"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING)
                                .description("상품 제목"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("상품 썸네일 파일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER)
                                .description("판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER)
                                .description("할인율"),
                        fieldWithPath("data.dataList.[].author").type(JsonFieldType.ARRAY)
                                .description("저자명"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN)
                                .description("강제 품절 상태"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER)
                                .description("상품 재고량"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING)
                                .description("상품 출판사")
                )
        ));
    }
}
