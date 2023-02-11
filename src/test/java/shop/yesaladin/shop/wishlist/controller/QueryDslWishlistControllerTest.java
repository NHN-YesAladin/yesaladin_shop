package shop.yesaladin.shop.wishlist.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.QueryDslWishlistService;

@AutoConfigureRestDocs
@WebMvcTest(QueryDslWishlistController.class)
class QueryDslWishlistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryDslWishlistService queryDslWishlistService;

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
    }
}