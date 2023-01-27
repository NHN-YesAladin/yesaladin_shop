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
    @DisplayName("상품의 제목으로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByTitleOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String title = "title";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(title);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/title")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        verify(searchProductService, never()).searchProductsByProductTitle(title, -1, 1);

        //doc
        resultActions.andDo(
                document(
                        "search-product-fail-offset-minus-validation-error",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("query").type(JsonFieldType.STRING)
                                        .description("검색 내용"),
                                fieldWithPath("offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 위치"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("데이터 수")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("에러 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByTitleSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String title = "title";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(title);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/title")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductTitle(title, -1, 1);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByTitleSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String title = "title";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(title);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/title")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductTitle(title, -1, 1);
    }

    @Test
    @DisplayName("상품의 제목으로 검색 성공")
    void testSearchProductByTitleSuccess() throws Exception {
        //given
        String title = "title";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(title);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByProductTitle(title, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/title")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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

        verify(searchProductService, atLeastOnce()).searchProductsByProductTitle(title, 0, 1);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByContentOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery("content");
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/content")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByContentSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String content = "content";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(content);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/content")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductContent(content, -1, 1);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchContentByTitleSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String content = "content";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(content);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/content")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductContent(content, -1, 1);
    }

    @Test
    @DisplayName("상품의 내용으로 검색 성공")
    void testSearchProductByContentSuccess() throws Exception {
        //given
        String content = "content";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(content);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByProductContent(content, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/content")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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

        verify(searchProductService, atLeastOnce()).searchProductsByProductContent(content, 0, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByISBNOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String ISBN = "isbn";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(ISBN);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, -1, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByISBNSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String ISBN = "isbn";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(ISBN);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, -1, 1);
    }

    @Test
    @DisplayName("상품의 ISBN으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByISBNSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String ISBN = "isbn";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(ISBN);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductISBN(ISBN, -1, 1);
    }

    @Test
    @DisplayName("상품의 isbn으로 검색 성공")
    void testSearchProductByISBNSuccess() throws Exception {
        //given
        String ISBN = "isbn";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(ISBN);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByProductISBN(ISBN, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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
    @DisplayName("작가 이름으로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByAuthorOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String Author = "author";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(Author);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/author")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductAuthor(Author, -1, 1);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByAuthorSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String author = "author";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(author);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/author")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductAuthor(author, -1, 1);
    }

    @Test
    @DisplayName("작가 이름으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByAuthorSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String author = "author";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(author);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/author")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByProductAuthor(author, -1, 1);
    }

    @Test
    @DisplayName("작가의 이름으로 검색 성공")
    void testSearchProductByAuthorSuccess() throws Exception {
        //given
        String author = "author";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(author);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByProductAuthor(author, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/author")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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

        verify(searchProductService, atLeastOnce()).searchProductsByProductAuthor(author, 0, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByPublisherOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String publisher = "publisher";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(publisher);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/publisher")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByPublisher(publisher, -1, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByPublisherSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String publisher = "publisher";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(publisher);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/publisher")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByPublisher(publisher, -1, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByPublisherSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String publisher = "publisher";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(publisher);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/publisher")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByPublisher(publisher, -1, 1);
    }

    @Test
    @DisplayName("출판사 이름으로 검색 성공")
    void testSearchProductByPublisherSuccess() throws Exception {
        //given
        String publisher = "publisher";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(publisher);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByPublisher(publisher, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/publisher")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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

        verify(searchProductService, atLeastOnce()).searchProductsByPublisher(publisher, 0, 1);
    }

    @Test
    @DisplayName("태그로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByTagOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String tag = "tag";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(tag);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(tag, -1, 1);
    }

    @Test
    @DisplayName("태그으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByTagSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String tag = "tag";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(tag);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(tag, -1, 1);
    }

    @Test
    @DisplayName("태그로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByTagSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String tag = "tag";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(tag);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByTag(tag, -1, 1);
    }

    @Test
    @DisplayName("태그로 검색 성공")
    void testSearchProductByTagSuccess() throws Exception {
        //given
        String tag = "tag";
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(tag);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByTag(tag, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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

        verify(searchProductService, atLeastOnce()).searchProductsByTag(tag, 0, 1);
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByCategoryIdOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String categoryId = "1";

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/id/".concat(
                categoryId))
                .accept(MediaType.APPLICATION_JSON)
                .param("offset", "-1")
                .param("size", "1"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(
                Long.parseLong(categoryId),
                -1,
                1
        );
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByCategoryIdSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String categoryId = "1";
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/id/".concat(
                categoryId))
                .accept(MediaType.APPLICATION_JSON)
                .param("offset", "0")
                .param("size", "0"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(
                Long.parseLong(categoryId),
                0,
                0
        );
    }

    @Test
    @DisplayName("카테고리 id로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByCategoryIdSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String categoryId = "1";
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/id/".concat(
                categoryId))
                .accept(MediaType.APPLICATION_JSON)
                .param("offset", "0")
                .param("size", "0"));
        //then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryId(
                Long.parseLong(categoryId),
                -1,
                1
        );
    }

    @Test
    @DisplayName("카테고리 id로 검색 성공")
    void testSearchProductByCategoryIdSuccess() throws Exception {
        //given
        Long categoryId = dto.getCategories().get(0).getId();
        Mockito.when(searchProductService.searchProductsByCategoryId(categoryId, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get(
                "/search/product/category/id/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("offset", "0")
                .param("size", "1"));
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

        verify(searchProductService, atLeastOnce()).searchProductsByCategoryId(categoryId, 0, 1);
    }

    @Test
    @DisplayName("카테고리 이름로 검색 시 페이지 위치가 0보다 작을 경우 BadRequest")
    void testSearchProductByCategoryNameOffsetLessThanZeroThrBadRequest() throws Exception {
        //given
        String categoryName = dto.getCategories().get(0).getName();
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(categoryName);
        requestDto.setOffset(-1);
        requestDto.setSize(1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(categoryName, -1, 1);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 시 요청갯수가 1보다 작을 경우 BadRequest")
    void testSearchProductByCategoryNameSizeLessThanOneThrBadRequest() throws Exception {
        //given
        String categoryName = dto.getCategories().get(0).getName();
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(categoryName);
        requestDto.setOffset(0);
        requestDto.setSize(0);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(categoryName, -1, 1);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 시 요청갯수가 20보다 클 경우 BadRequest")
    void testSearchProductByCategoryNameSizeMoreThanTwentyThrBadRequest() throws Exception {
        //given
        String categoryName = dto.getCategories().get(0).getName();
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(categoryName);
        requestDto.setOffset(0);
        requestDto.setSize(21);
        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(searchProductService, never()).searchProductsByCategoryName(categoryName, -1, 1);
    }

    @Test
    @DisplayName("카테고리 이름으로 검색 성공")
    void testSearchProductByCategoryNameSuccess() throws Exception {
        //given
        String categoryName = dto.getCategories().get(0).getName();
        SearchProductRequestDto requestDto = new SearchProductRequestDto();
        requestDto.setQuery(categoryName);
        requestDto.setOffset(0);
        requestDto.setSize(1);

        Mockito.when(searchProductService.searchProductsByCategoryName(categoryName, 0, 1))
                .thenReturn(dummy);

        //when
        ResultActions resultActions = mockMvc.perform(get("/search/product/category/name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
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
                categoryName,
                0,
                1
        );
    }
}