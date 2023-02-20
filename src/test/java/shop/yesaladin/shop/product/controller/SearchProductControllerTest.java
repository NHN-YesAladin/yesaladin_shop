package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;
import shop.yesaladin.shop.product.service.inter.SearchProductService;

@AutoConfigureRestDocs
@WebMvcTest(SearchProductController.class)
class SearchProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchProductService searchProductService;

    @Autowired
    ObjectMapper objectMapper;
    SearchedProductResponseDto responseDto;
    String over = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @BeforeEach
    void setUp() {
        responseDto = SearchedProductResponseDto.builder()
                .id(1L)
                .title("title")
                .isbn("isbn")
                .quantity(1000L)
                .sellingPrice(1000L)
                .rate(10)
                .isForcedOutOfStock(false)
                .isSubscriptionAvailable(false)
                .isEbook(false)
                .publisher("publisher")
                .publishedDate(LocalDate.of(2000, 10, 10))
                .authors(List.of("author"))
                .tags(List.of("tags"))
                .thumbnailFile("file")
                .build();
    }

    @WithMockUser
    @Test
    void searchProductByTitle_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("title", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-title-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("title").description("검색할 제목"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품의 제목으로 검색 성공")
    void testSearchProductByTitleSuccess() throws Exception {
        Mockito.when(searchProductService.searchProductsByProductTitle(
                        "title",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("title", "title"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-title",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("title").description("검색할 제목"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트")
                )
        ));
    }

    @WithMockUser
    @Test
    void searchProductByContent_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("content", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-content-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("content").description("검색할 내용"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품의 내용으로 검색 성공")
    void testSearchProductByContentSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductContent(
                        "content",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("content", "content"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-content",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("content").description("검색할 내용"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    void searchProductByISBN_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("isbn", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-isbn-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("isbn").description("검색할 isbn"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품의 isbn으로 검색 성공")
    void testSearchProductByISBNSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductISBN(
                        "isbn",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", "isbn"));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-isbn",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("isbn").description("검색할 isbn"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    void searchProductByAuthor_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("author", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-author-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("author").description("검색할 저자"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("작가의 이름으로 검색 성공")
    void testSearchProductByAuthorSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductAuthor(
                        "author",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("author", "author"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-author",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("author").description("검색할 저자"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    void searchProductByPublisher_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("publisher", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-publisher-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("publisher").description("검색할 출판사"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("출판사 이름으로 검색 성공")
    void testSearchProductByPublisherSuccess() throws Exception {
        //when
        Mockito.when(searchProductService.searchProductsByPublisher(
                        "publisher",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", "publisher"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("publisher").description("검색할 출판사"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    void searchProductByTag_overMaximumInputLength() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("tag", over));

        resultActions.andExpect(status().is5xxServerError());

        resultActions.andDo(document(
                "search-product-fail-search-tag-length-over",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("tag").description("검색할 태그"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("errorMessage")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("태그로 검색 성공")
    void testSearchProductByTagSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByTag("tag", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", "tag"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-search-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("tag").description("검색할 태그"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    @DisplayName("카테고리 id로 검색 성공")
    void testSearchProductByCategoryIdSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByCategoryId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", "1"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-category-id",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("categoryid").description("검색할 카테고리 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }

    @WithMockUser
    @Test
    @DisplayName("카테고리 이름으로 검색 성공")
    void testSearchProductByCategoryNameSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByCategoryName(
                        "name",
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", "name"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].isbn", equalTo(responseDto.getIsbn())))
                .andExpect(jsonPath("$.data.dataList[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList[0].rate", equalTo(responseDto.getRate())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isForcedOutOfStock",
                        equalTo(responseDto.getIsForcedOutOfStock())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].thumbnailFile",
                        equalTo(responseDto.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].authors[0]",
                        equalTo(responseDto.getAuthors().get(0))
                ))
                .andExpect(jsonPath(
                        "$.data.dataList[0].tags[0]",
                        equalTo(responseDto.getTags().get(0))
                ));

        resultActions.andDo(document("search-product-success-categoryname",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("categoryname").description("검색할 카테고리 이름"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("검색 전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현쟈 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 갯수"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY).description("검색 결과 상품 리스트"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("상품의 제목"),
                        fieldWithPath("data.dataList.[].isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("상품의 출판사"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("상품의 갯수"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("상품의 할인율"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("상품의 강제 품절 여부"),
                        fieldWithPath("data.dataList.[].isEbook").type(JsonFieldType.BOOLEAN).description("E-book 여부"),
                        fieldWithPath("data.dataList.[].isSubscriptionAvailable").type(JsonFieldType.BOOLEAN).description("구독 상품 여부"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
                        fieldWithPath("data.dataList.[].thumbnailFile").type(JsonFieldType.STRING).description("상품의 썸네일 사진"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("상품의 저자 리스트"),
                        fieldWithPath("data.dataList.[].tags.[]").type(JsonFieldType.ARRAY).description("상품의 태그 리스트"))));
    }
}
