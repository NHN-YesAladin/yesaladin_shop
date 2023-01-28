package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedCategories;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedFile;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTags;
import shop.yesaladin.shop.product.dto.SearchProductRequestDto;
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

    SearchedProductResponseDto dto;
    List<SearchedProductResponseDto> dummy;
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

    @BeforeEach
    void setUp() {
        dto = SearchedProductResponseDto.builder()
                .id(-1L)
                .title("title")
                .discountRate(10)
                .sellingPrice(1000L)
                .authors(List.of(new SearchedAuthor(1L, "author")))
                .isForcedOutOfStack(false)
                .thumbnailFileUrl(new SearchedFile(1L, "깃 허브.jpg", LocalDate.now()))
                .publishedDate(LocalDate.now().toString())
                .publisher(new SearchedPublisher(1L, "publisher"))
                .categories(List.of(new SearchedCategories(1L, null, "국내소설", true, false)))
                .authors(List.of(new SearchedAuthor(1L, "name")))
                .tags(List.of(new SearchedTags(1L, "tag1")))
                .build();

        dummy = List.of(dto);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTitleOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())))
                .andDo(print());

        verify(searchProductService, atLeastOnce()).searchProductsByProductTitle(TITLE, 0, 1);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByContentOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        Mockito.when(searchProductService.searchProductsByProductContent(CONTENT, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("content", CONTENT)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByProductContent(CONTENT, 0, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByISBNOffsetLessThanZeroConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("isbn", ISBN)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByProductISBN(ISBN, 0, 1);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByAuthorOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("author", AUTHOR)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByProductAuthor(AUTHOR, 0, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByPublisherOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("publisher", PUBLISHER)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByPublisher(PUBLISHER, 0, 1);
    }

    @Test
    @DisplayName("태그로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByTagOffsetLessThanZeroConstraintViolationException() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("tag", TAG)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByTag(TAG, 0, 1);
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryIdOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        Mockito.when(searchProductService.searchProductsByCategoryId(dto.getCategories()
                        .get(0)
                        .getId(), 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryid", CATEGORY_ID)
                .param("offset", ZERO)
                .param("size", ONE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByCategoryId(dto.getCategories()
                .get(0)
                .getId(), 0, 1);
    }

    @Test
    @DisplayName("카테고리 이름로 검색 시 페이지 위치가 0보다 작을 경우 ConstraintViolationException")
    void testSearchProductByCategoryNameOffsetLessThanZeroThrConstraintViolationException()
            throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
        ResultActions resultActions = mockMvc.perform(get("/search/products")
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
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/products")
                .accept(MediaType.APPLICATION_JSON)
                .param("categoryname", CATEGORY_NAME)
                .param("offset", ZERO)
                .param("size", ONE));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(-1)))
                .andExpect(jsonPath("$[0].title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$[0].discountRate", equalTo(dto.getDiscountRate())))
                .andExpect(jsonPath("$[0].sellingPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$[0].isForcedOutOfStack",
                        equalTo(dto.getIsForcedOutOfStack())
                ))
                .andExpect(jsonPath("$[0].publishedDate", equalTo(dto.getPublishedDate())))
                .andExpect(jsonPath("$[0].thumbnailFileUrl.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.name",
                        equalTo(dto.getThumbnailFileUrl().getName())
                ))
                .andExpect(jsonPath(
                        "$[0].thumbnailFileUrl.uploadDateTime",
                        equalTo(dto.getThumbnailFileUrl().getUploadDateTime().toString())
                ))
                .andExpect(jsonPath("$[0].publisher.id", equalTo(1)))
                .andExpect(jsonPath("$[0].publisher.name", equalTo(dto.getPublisher().getName())))
                .andExpect(jsonPath("$[0].categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].categories[0].name",
                        equalTo(dto.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].parent",
                        equalTo(dto.getCategories().get(0).getParent())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].isShown",
                        equalTo(dto.getCategories().get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$[0].categories[0].disable",
                        equalTo(dto.getCategories().get(0).getDisable())
                ))
                .andExpect(jsonPath("$[0].authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$[0].authors[0].name",
                        equalTo(dto.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$[0].tags[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].tags[0].name", equalTo(dto.getTags().get(0).getName())));

        verify(searchProductService, atLeastOnce()).searchProductsByCategoryName(
                CATEGORY_NAME,
                0,
                1
        );
    }
}