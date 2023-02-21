package shop.yesaladin.shop.wishlist.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.time.LocalDateTime;
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
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.CommandWishlistService;

@AutoConfigureRestDocs
@WebMvcTest(CommandWishlistController.class)
class CommandWishlistControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CommandWishlistService commandWishlistService;

    @WithMockUser
    @Test
    @DisplayName("save 시 MemberNotFound")
    void save_MemberNotFound() throws Exception {
        Long productId = 1L;

        Mockito.when(commandWishlistService.save(any(), eq(productId)))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        ResultActions resultActions = mockMvc.perform(get("/v1/wishlist").with(csrf()).queryParam(
                "productid",
                "1"
        ));

        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));
        resultActions.andDo(document(
                "wishlist-save-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 등록할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional())
                ));
    }

    @WithMockUser
    @Test
    @DisplayName("save 시 ProductNotFound")
    void save_ProductNotFound() throws Exception {
        Long productId = 1L;

        Mockito.when(commandWishlistService.save(any(), eq(productId))).thenThrow(
                new ProductNotFoundException(productId));

        ResultActions resultActions = mockMvc.perform(get("/v1/wishlist").with(csrf()).queryParam(
                "productid",
                "1"
        ));

        resultActions.andExpect(status().isNotFound());

        resultActions.andDo(document(
                "wishlist-save-product-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 등록할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("message").type(JsonFieldType.STRING).description("에러메시지")
        )));
    }

    @WithMockUser
    @Test
    @DisplayName("위시리스트 등록 성공")
    void save_success() throws Exception {
        Long productId = 1L;
        LocalDateTime localDate = LocalDateTime.now();

        Mockito.when(commandWishlistService.save(any(), eq(productId)))
                .thenReturn(new WishlistSaveResponseDto(productId, localDate));

        ResultActions resultActions = mockMvc.perform(get("/v1/wishlist").with(csrf()).queryParam(
                "productid",
                "1"
        ));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", equalTo(201)))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath(
                        "$.data.registeredDateTime",
                        containsString(localDate.toString()
                                .substring(0, localDate.toString().length() - 3))
                ))
                .andDo(print());

        resultActions.andDo(document(
                "wishlist-save-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 등록할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("null")
                                .optional(),
                        fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("위시리스트에 등록된 상품 id"),
                        fieldWithPath("data.registeredDateTime").type(JsonFieldType.STRING).description("위시르스트에 등록된 날짜"))
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("delete 시 MemberNotFound")
    void delete_MemberNotFound() throws Exception {
        //given
        Mockito.doThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""))
                .when(commandWishlistService)
                .delete(any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(delete("/v1/wishlist").with(csrf())
                .queryParam(
                        "productid",
                        "1"
                ));

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));

        resultActions.andDo(document(
                "wishlist-delete-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 삭제할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional())
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("delete 시 WishlistNotFound")
    void delete_WishlistNotFound() throws Exception {
        //given
        Mockito.doThrow(new ClientException(ErrorCode.BAD_REQUEST, ""))
                .when(commandWishlistService)
                .delete(any(), any());
        ;

        //when
        ResultActions resultActions = mockMvc.perform(delete("/v1/wishlist").with(csrf())
                .queryParam(
                        "productid",
                        "1"
                ));

        //then
        resultActions.andExpect(status().isBadRequest());

        resultActions.andDo(document(
                "wishlist-delete-wishlist-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 삭제할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러메시지")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional())
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("delete 성공")
    void delete_success() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(delete("/v1/wishlist").with(csrf())
                .queryParam(
                        "productid",
                        "1"
                ));

        resultActions.andExpect(status().isNoContent());
        verify(commandWishlistService, atLeastOnce()).delete(any(), any());

        resultActions.andDo(document(
                "wishlist-delete-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("productid").description("위시리스트에 삭제할 상품 id"),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("null")
                                .optional(),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("null").optional())
        ));
    }
}
