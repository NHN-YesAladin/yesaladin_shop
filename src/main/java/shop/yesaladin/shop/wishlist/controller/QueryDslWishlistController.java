package shop.yesaladin.shop.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.QueryDslWishlistService;

/**
 * 위시리스트 조회 컨트롤러
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/wishlist")
public class QueryDslWishlistController {
    private final QueryDslWishlistService queryDslWishlistService;

    /**
     * 위시리스트 조회 메서드
     *
     * @param loginId 조회할 위시리스트의 회원 로그인 아이디
     * @param pageable 페이지 정보
     * @return 조회된 위시리스트와 상태 및 페이지 정보
     */
    @PostMapping
    ResponseDto<PaginatedResponseDto<WishlistResponseDto>> findWishlistByMemberId(
            @LoginId(required = true) String loginId,
            @PageableDefault Pageable pageable
    ) {
        Page<WishlistResponseDto> wishlists = queryDslWishlistService.findWishlistByMemberId(
                loginId,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<WishlistResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(PaginatedResponseDto.<WishlistResponseDto>builder()
                        .dataList(wishlists.getContent())
                        .totalPage(wishlists.getTotalPages())
                        .currentPage(wishlists.getNumber())
                        .totalDataCount(wishlists.getTotalElements())
                        .build())
                .build();
    }
}
