package shop.yesaladin.shop.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.QueryWishlistService;

/**
 * 위시리스트 조회 컨트롤러
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/wishlist")
public class QueryWishlistController {

    private final QueryWishlistService queryWishlistService;

    /**
     * 위시리스트 조회 메서드
     *
     * @param loginId  조회할 위시리스트의 회원 로그인 아이디
     * @param pageable 페이지 정보
     * @return 조회된 위시리스트와 상태 및 페이지 정보
     */
    @PostMapping
    public ResponseDto<PaginatedResponseDto<WishlistResponseDto>> findWishlistByMemberId(
            @LoginId(required = true) String loginId,
            @PageableDefault Pageable pageable
    ) {
        Page<WishlistResponseDto> wishlists = queryWishlistService.findWishlistByMemberId(
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

    /**
     * 위시리스트 등록 유무 확인
     *
     * @param loginId   해당 위시리스트의 유저 loginId
     * @param productId 확인할 상품의 id
     * @return 등록되어 있다면 true 아니면 false
     */
    @GetMapping("/existence")
    public ResponseDto<Boolean> isExist(
            @LoginId(required = true) String loginId,
            @RequestParam(name = "productid") Long productId
    ) {
        return ResponseDto.<Boolean>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(queryWishlistService.isExists(loginId, productId))
                .build();
    }
}
