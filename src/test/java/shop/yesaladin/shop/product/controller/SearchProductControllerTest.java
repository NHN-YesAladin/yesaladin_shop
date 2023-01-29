package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductFile;
import shop.yesaladin.shop.product.domain.model.SearchedProductProductType;
import shop.yesaladin.shop.product.domain.model.SearchedProductSubscribProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.dto.SearchedProductDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerResponseDto;
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

    SearchedProductDto dummySearchedProductDto;
    SearchedProductResponseDto dummySearchedProductResponseDto;
    SearchedProductManagerResponseDto dummySearchedProductManagerResponseDto;
    SearchedProductManagerDto dummySearchedProductManagerDto;
    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MIN = "-1";
    private static final String TWOONE = "21";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String ISBN = "isbn";
    private static final String AUTHOR = "author";
    private static final String PUBLISHER = "publisher";
    private static final String TAG = "tag";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "name";
    private static final long COUNT = 1;
    private static final int INT_ONE = 1;
    private static final int INT_MIN = -1;

    @BeforeEach
    void setUp() {
        dummySearchedProductDto = SearchedProductDto.builder()
                .id(-1L)
                .title("title")
                .discountRate(10)
                .sellingPrice(1000L)
                .authors(List.of("author"))
                .isForcedOutOfStack(false)
                .thumbnailFileUrl("깃 허브.jpg")
                .publishedDate(LocalDate.now().toString())
                .categories(List.of(new SearchedProductCategory(12L, null, "국내소설", true, false)))
                .tags(List.of("tag1"))
                .build();

        dummySearchedProductResponseDto = SearchedProductResponseDto.builder()
                .products(List.of(dummySearchedProductDto))
                .count(COUNT)
                .build();

        dummySearchedProductManagerDto = SearchedProductManagerDto.builder()
                .id(-1L)
                .isbn("isbn")
                .title("title")
                .actualPrice(1000L)
                .discountRate(10)
                .isSeparatelyDiscount(false)
                .givenPointRate(10)
                .isGivenPoint(false)
                .isSale(false)
                .quantity(1000L)
                .preferentialShowRanking(1000L)
                .productType(new SearchedProductProductType(1, "type"))
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(1, 10))
                .thumbnailFile(new SearchedProductFile(1L, "file1", LocalDate.now()))
                .ebookFile(new SearchedProductFile(2L, "file2", LocalDate.now()))
                .publishedDate(LocalDate.now())
                .savingMethod("saving")
                .categories(List.of(new SearchedProductCategory(1L, null, "name", true, false)))
                .authors(List.of(new SearchedProductAuthor(1L, "name")))
                .tags(List.of(new SearchedProductTag(1L, "tag")))
                .subscribeProducts(List.of(new SearchedProductSubscribProduct(1L, "issn")))
                .build();

        dummySearchedProductManagerResponseDto = SearchedProductManagerResponseDto.builder()
                .products(List.of(dummySearchedProductManagerDto))
                .count(COUNT)
                .build();
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTitleOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError());

        verify(searchProductService, never()).searchProductsByProductTitle(TITLE, -1, 1);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTitleSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByProductTitle(TITLE, 0, 0);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByTitleSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByProductTitle(TITLE, 0, 21);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 성공")
    void testSearchProductByTitleSuccess() throws Exception {
        Mockito.when(searchProductService.searchProductsByProductTitle(TITLE, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByProductTitle(TITLE, 0, 1);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByContentOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("content", CONTENT)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductContent(CONTENT, -1, 0);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByContentSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("content", CONTENT)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByProductContent(CONTENT, 0, 0);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchContentByTitleSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("content", CONTENT)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByProductContent(CONTENT, 0, 21);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 성공")
    void testSearchProductByContentSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductContent(CONTENT, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("content", CONTENT)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());


        verify(searchProductService, atLeastOnce()).searchProductsByProductContent(CONTENT, 0, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByISBNOffsetLessThanZeroConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", ISBN)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, -1, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByISBNSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", ISBN)
                .param("offset", ZERO)
                .param("size", MIN));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, 0, 0);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByISBNSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", ISBN)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, 0, 21);
    }

    @Test
    @DisplayName("상품의 isbn으로 검색 성공")
    void testSearchProductByISBNSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductISBN(ISBN, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", ISBN)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());


        verify(searchProductService, atLeastOnce()).searchProductsByProductISBN(ISBN, 0, 1);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByAuthorOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("author", AUTHOR)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductAuthor(AUTHOR, -1, 1);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByAuthorSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("author", AUTHOR)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductAuthor(AUTHOR, 0, 0);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByAuthorSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("author", AUTHOR)
                .param("offset", ONE)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());
        verify(searchProductService, never()).searchProductsByProductAuthor(AUTHOR, 0, 21);
    }

    @Test
    @DisplayName("작가의 이름으로 검색 성공")
    void testSearchProductByAuthorSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByProductAuthor(AUTHOR, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("author", AUTHOR)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByPublisherOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", PUBLISHER)
                .param("offset", MIN)
                .param("size", ONE));

        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
        verify(searchProductService, never()).searchProductsByPublisher(PUBLISHER, -1, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByPublisherSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", PUBLISHER)
                .param("offset", MIN)
                .param("size", ONE));

        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
        verify(searchProductService, never()).searchProductsByPublisher(PUBLISHER, -1, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByPublisherSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", PUBLISHER)
                .param("offset", MIN)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByPublisher(PUBLISHER, 0, 21);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 성공")
    void testSearchProductByPublisherSuccess() throws Exception {
        //when
        Mockito.when(searchProductService.searchProductsByPublisher(PUBLISHER, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", PUBLISHER)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByPublisher(PUBLISHER, 0, 1);
    }

    @Test
    @DisplayName("태그로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTagOffsetLessThanZeroConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", TAG)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(TAG, -1, 1);
    }

    @Test
    @DisplayName("태그으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTagSizeLessThanOneThrConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", TAG)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(TAG, 0, 0);
    }

    @Test
    @DisplayName("태그로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByTagSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", TAG)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(TAG, 0, 21);
    }

    @Test
    @DisplayName("태그로 검색 성공")
    void testSearchProductByTagSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByTag(TAG, 0, 1))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", TAG)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductDto.getTitle())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(dummySearchedProductDto.getQuantity())))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].isForcedOutOfStack", equalTo(dummySearchedProductDto.getIsForcedOutOfStack())))
                .andExpect(jsonPath("$.products[0].thumbnailFileUrl", equalTo(dummySearchedProductDto.getThumbnailFileUrl())))
                .andExpect(jsonPath("$.products[0].publisher", equalTo(dummySearchedProductDto.getPublisher())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductDto.getPublishedDate())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(12)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0]", equalTo(dummySearchedProductDto.getAuthors().get(0))))
                .andExpect(jsonPath("$.products[0].tags[0]", equalTo(dummySearchedProductDto.getTags().get(0))))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByTag(TAG, 0, 1);
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryIdOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", CATEGORY_ID)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(1L, -1, 1);
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryIdSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", CATEGORY_ID)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(1L, 0, 0);
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByCategoryIdSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", CATEGORY_ID)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError()).andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(1L, 0, 21);
    }

    @Test
    @DisplayName("카테고리 id로 검색 성공")
    void testSearchProductByCategoryIdSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByCategoryId(dummySearchedProductManagerDto.getCategories().get(0).getId(), 0, 1))
                .thenReturn(dummySearchedProductManagerResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", CATEGORY_ID)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductManagerDto.getTitle())))
                .andExpect(jsonPath("$.products[0].isbn", equalTo(dummySearchedProductManagerDto.getIsbn())))
                .andExpect(jsonPath("$.products[0].actualPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductManagerDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].separatelyDiscount", equalTo(dummySearchedProductManagerDto.isSeparatelyDiscount())))
                .andExpect(jsonPath("$.products[0].givenPointRate", equalTo(dummySearchedProductManagerDto.getGivenPointRate())))
                .andExpect(jsonPath("$.products[0].isGivenPoint", equalTo(dummySearchedProductManagerDto.getIsGivenPoint())))
                .andExpect(jsonPath("$.products[0].isSale", equalTo(dummySearchedProductManagerDto.getIsSale())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].productType.id", equalTo(1)))
                .andExpect(jsonPath("$.products[0].productType.name", equalTo(dummySearchedProductManagerDto.getProductType().getName())))
                .andExpect(jsonPath("$.products[0].searchedTotalDiscountRate.id", equalTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getId())))
                .andExpect(jsonPath("$.products[0].searchedTotalDiscountRate.discountRate", equalTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getDiscountRate())))
                .andExpect(jsonPath("$.products[0].thumbnailFile.id", equalTo(1)))
                .andExpect(jsonPath("$.products[0].thumbnailFile.name", equalTo(dummySearchedProductManagerDto.getThumbnailFile().getName())))
                .andExpect(jsonPath("$.products[0].thumbnailFile.uploadDateTime", equalTo(dummySearchedProductManagerDto.getThumbnailFile().getUploadDateTime().toString())))
                .andExpect(jsonPath("$.products[0].ebookFile.id", equalTo(2)))
                .andExpect(jsonPath("$.products[0].ebookFile.name", equalTo(dummySearchedProductManagerDto.getEbookFile().getName())))
                .andExpect(jsonPath("$.products[0].ebookFile.uploadDateTime", equalTo(dummySearchedProductManagerDto.getEbookFile().getUploadDateTime().toString())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductManagerDto.getPublishedDate().toString())))
                .andExpect(jsonPath("$.products[0].savingMethod", equalTo(dummySearchedProductManagerDto.getSavingMethod())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].authors[0].name", equalTo(dummySearchedProductManagerDto.getAuthors().get(0).getName())))
                .andExpect(jsonPath("$.products[0].tags[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].tags[0].name", equalTo(dummySearchedProductManagerDto.getTags().get(0).getName())))
                .andExpect(jsonPath("$.products[0].subscribeProducts[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].subscribeProducts[0].issn", equalTo(dummySearchedProductManagerDto.getSubscribeProducts().get(0).getIssn())))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByCategoryId(dummySearchedProductManagerDto.getCategories()
                .get(0)
                .getId(), 0, 1);
    }

    @Test
    @DisplayName("카테고리 이름로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", CATEGORY_NAME)
                .param("offset", MIN)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(CATEGORY_NAME, -1, 1);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 시 요청갯수가 1보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryNameSizeLessThanOneThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", CATEGORY_NAME)
                .param("offset", ZERO)
                .param("size", ZERO));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(CATEGORY_NAME, 0, 0);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 시 요청갯수가 20보다 클 경우 ConstraintViolationException")
    void testSearchProductByCategoryNameSizeMoreThanTwentyThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", CATEGORY_NAME)
                .param("offset", ZERO)
                .param("size", TWOONE));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(CATEGORY_NAME, 0, 21);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 성공")
    void testSearchProductByCategoryNameSuccess() throws Exception {
        //given
        Mockito.when(searchProductService.searchProductsByCategoryName(CATEGORY_NAME, 0, 1))
                .thenReturn(dummySearchedProductManagerResponseDto);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", CATEGORY_NAME)
                .param("offset", ZERO)
                .param("size", ONE));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].id", equalTo(INT_MIN)))
                .andExpect(jsonPath("$.products[0].title", equalTo(dummySearchedProductManagerDto.getTitle())))
                .andExpect(jsonPath("$.products[0].isbn", equalTo(dummySearchedProductManagerDto.getIsbn())))
                .andExpect(jsonPath("$.products[0].actualPrice", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].discountRate", equalTo(dummySearchedProductManagerDto.getDiscountRate())))
                .andExpect(jsonPath("$.products[0].separatelyDiscount", equalTo(dummySearchedProductManagerDto.isSeparatelyDiscount())))
                .andExpect(jsonPath("$.products[0].givenPointRate", equalTo(dummySearchedProductManagerDto.getGivenPointRate())))
                .andExpect(jsonPath("$.products[0].isGivenPoint", equalTo(dummySearchedProductManagerDto.getIsGivenPoint())))
                .andExpect(jsonPath("$.products[0].isSale", equalTo(dummySearchedProductManagerDto.getIsSale())))
                .andExpect(jsonPath("$.products[0].quantity", equalTo(1000)))
                .andExpect(jsonPath("$.products[0].productType.id", equalTo(1)))
                .andExpect(jsonPath("$.products[0].productType.name", equalTo(dummySearchedProductManagerDto.getProductType().getName())))
                .andExpect(jsonPath("$.products[0].searchedTotalDiscountRate.id", equalTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getId())))
                .andExpect(jsonPath("$.products[0].searchedTotalDiscountRate.discountRate", equalTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getDiscountRate())))
                .andExpect(jsonPath("$.products[0].thumbnailFile.id", equalTo(1)))
                .andExpect(jsonPath("$.products[0].thumbnailFile.name", equalTo(dummySearchedProductManagerDto.getThumbnailFile().getName())))
                .andExpect(jsonPath("$.products[0].thumbnailFile.uploadDateTime", equalTo(dummySearchedProductManagerDto.getThumbnailFile().getUploadDateTime().toString())))
                .andExpect(jsonPath("$.products[0].ebookFile.id", equalTo(2)))
                .andExpect(jsonPath("$.products[0].ebookFile.name", equalTo(dummySearchedProductManagerDto.getEbookFile().getName())))
                .andExpect(jsonPath("$.products[0].ebookFile.uploadDateTime", equalTo(dummySearchedProductManagerDto.getEbookFile().getUploadDateTime().toString())))
                .andExpect(jsonPath("$.products[0].publishedDate", equalTo(dummySearchedProductManagerDto.getPublishedDate().toString())))
                .andExpect(jsonPath("$.products[0].savingMethod", equalTo(dummySearchedProductManagerDto.getSavingMethod())))
                .andExpect(jsonPath("$.products[0].categories[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].categories[0].parent", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getParent())))
                .andExpect(jsonPath("$.products[0].categories[0].name", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getName())))
                .andExpect(jsonPath("$.products[0].categories[0].isShown", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getIsShown())))
                .andExpect(jsonPath("$.products[0].categories[0].disable", equalTo(dummySearchedProductManagerDto.getCategories().get(0).getDisable())))
                .andExpect(jsonPath("$.products[0].authors[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].authors[0].name", equalTo(dummySearchedProductManagerDto.getAuthors().get(0).getName())))
                .andExpect(jsonPath("$.products[0].tags[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].tags[0].name", equalTo(dummySearchedProductManagerDto.getTags().get(0).getName())))
                .andExpect(jsonPath("$.products[0].subscribeProducts[0].id", equalTo(INT_ONE)))
                .andExpect(jsonPath("$.products[0].subscribeProducts[0].issn", equalTo(dummySearchedProductManagerDto.getSubscribeProducts().get(0).getIssn())))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByCategoryName(
                CATEGORY_NAME,
                0,
                1
        );
    }
}