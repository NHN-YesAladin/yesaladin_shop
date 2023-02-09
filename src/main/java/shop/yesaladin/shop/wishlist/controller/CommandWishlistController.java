package shop.yesaladin.shop.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.CommandWishlistService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/wishlist")
public class CommandWishlistController {

    private final CommandWishlistService commandWishlistService;

    /**
     * 위시리스트 저장 메서드
     *
     * @param loginId 회원의 로그인 아이디
     * @param productId 상품 id
     * @return 위시리스트 저장 결과
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping
    public ResponseDto<WishlistSaveResponseDto> save(
            @LoginId(required = true) String loginId,
            Long productId
    ) {
        return ResponseDto.<WishlistSaveResponseDto>builder()
                .status(HttpStatus.CREATED)
                .success(true)
                .data(commandWishlistService.save(loginId, productId))
                .build();
    }
}
