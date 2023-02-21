package shop.yesaladin.shop.wishlist.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    }
}
