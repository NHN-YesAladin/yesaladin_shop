package shop.yesaladin.shop.wishlist.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.time.LocalDateTime;
import java.util.List;
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
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.QueryWishlistService;

@AutoConfigureRestDocs
@WebMvcTest(QueryWishlistController.class)
class QueryWishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryWishlistService queryDslWishlistService;

    @WithMockUser
    @Test
    @DisplayName("findWishlistByMemberId 시 회원 못 참음")
    void findWishlistByMemberId_MemberNotFound() throws Exception {
        //given
        Mockito.when(queryDslWishlistService.findWishlistByMemberId(
                        any(),
                        eq(PageRequest.of(0, 10))
                ))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));
        //when
        ResultActions result = mockMvc.perform(post("/v1/wishlist").with(csrf()));
        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));
        //docs
        result.andDo(document(
                "find-wishlist-by-member-id-thr-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("_csrf").description("csrf")),
                responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("null")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("findWishlistByMemberId 시 출판사 못 참음")
    void findWishlistByMemberId_PublisherNotFound() throws Exception {
        //given
        Mockito.when(queryDslWishlistService.findWishlistByMemberId(
                        any(),
                        eq(PageRequest.of(0, 10))
                ))
                .thenThrow(new ClientException(ErrorCode.PUBLISH_NOT_FOUND, ""));
        //when
        ResultActions resultActions = mockMvc.perform(post("/v1/wishlist").with(csrf()));
        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.PUBLISH_NOT_FOUND.getDisplayName())
                ));

        //docs
        resultActions.andDo(document(
                "find-wishlist-by-member-id-thr-publisher-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("_csrf").description("csrf")),
                responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("null")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("findWishlistByMemberId 성공")
    void findWishlistByMemberId_success() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        WishlistResponseDto wishlistResponseDto = WishlistResponseDto.builder()
                .id(1L)
                .title("title")
                .thumbnailFileUrl("url")
                .sellingPrice(10000L)
                .rate(10)
                .publisher("publisher")
                .author(List.of("author"))
                .isForcedOutOfStock(false)
                .quantity(100L)
                .registeredDateTime(now)
                .build();
        Mockito.when(queryDslWishlistService.findWishlistByMemberId(
                        any(),
                        eq(PageRequest.of(0, 10))
                ))
                .thenReturn(new PageImpl<>(List.of(wishlistResponseDto)));

        ResultActions resultActions = mockMvc.perform(post("/v1/wishlist").with(csrf()));

        resultActions.andExpect(status().isOk());

        //docs
        resultActions.andDo(document(
                "find-wishlist-by-member-id-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("_csrf").description("csrf")),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("null")
                                .optional(),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체페이지 수"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("위시리스트에 등록된 상품의 id"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("위시리스트에 등록된 상품의 제목"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("위시리스트에 등록된 상품의 썸네일 파일 url"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("위시리스트에 등록된 상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("위시리스트에 등록된 상품의 할인율"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("위시리스트에 등록된 상품의 출판사명"),
                        fieldWithPath("data.dataList.[].author").type(JsonFieldType.ARRAY).description("위시리스트에 등록된 상품의 저자명 리스트"),
                        fieldWithPath("data.dataList.[].isForcedOutOfStock").type(JsonFieldType.BOOLEAN).description("위시리스트에 등록된 상품의 강제 품절 상태"),
                        fieldWithPath("data.dataList.[].quantity").type(JsonFieldType.NUMBER).description("위시리스트에 등록된 상품의 재고량"),
                        fieldWithPath("data.dataList.[].registeredDateTime").type(JsonFieldType.STRING).description("위시리스트에 등록된 날짜")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("isExists에서 MemberNotFound 발생")
    void isExists_MemberNotFound() throws Exception {
        //given
        Mockito.when(queryDslWishlistService.isExists(
                        any(),
                        eq(1L)
                ))
                .thenThrow(new ClientException(ErrorCode.PUBLISH_NOT_FOUND, ""));
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/wishlist/existence").queryParam(
                "productid",
                "1"
        ).with(csrf()));

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.PUBLISH_NOT_FOUND.getDisplayName())
                ));

        //docs
        resultActions.andDo(document(
                "exists-wishlist-thr-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("_csrf").description("csrf"),
                        parameterWithName("productid").description("상품 id")),
                responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("null")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("isExists에서 성공")
    void isExists_success() throws Exception {
        //given
        Mockito.when(queryDslWishlistService.isExists(
                        any(),
                        eq(1L)
                ))
                .thenReturn(true);
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/wishlist/existence").queryParam(
                "productid",
                "1"
        ).with(csrf()));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data", equalTo(true)));

        //docs
        resultActions.andDo(document(
                "exists-wishlist-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("_csrf").description("csrf"),
                        parameterWithName("productid").description("상품 id")),
                responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("null")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                .description("위시리스트 등록 여부")
                )
        ));
    }
}