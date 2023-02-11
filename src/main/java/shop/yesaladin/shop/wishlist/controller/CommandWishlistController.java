package shop.yesaladin.shop.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.CommandWishlistService;

/**
 * 위시리스트 등록 및 삭제 컨트롤러
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/wishlist")
public class CommandWishlistController {

    private final CommandWishlistService commandWishlistService;

    /**
     * 위시리스트 등록 메서드
     *
     * @param loginId   회원의 로그인 아이디
     * @param productId 상품 id
     * @return 위시리스트 저장 결과
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<WishlistSaveResponseDto> save(
            @LoginId(required = true) String loginId,
            @RequestParam(name = "productid") Long productId
    ) {
        return ResponseDto.<WishlistSaveResponseDto>builder()
                .status(HttpStatus.CREATED)
                .success(true)
                .data(commandWishlistService.save(loginId, productId))
                .build();
    }

    @GetMapping("/existence")
    public ResponseDto<Boolean> isExist(
            @LoginId(required = true) String loginId,
            @RequestParam(name = "productid") Long productId
    ) {
        return ResponseDto.<Boolean>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(commandWishlistService.isExists(loginId, productId))
                .build();
    }

    /**
     * 위시리스트 삭제 메서드
     *
     * @param loginId   위시리스트의 회원 loginId
     * @param productId 삭제할 위시리스트 상품
     * @return 삭제 결과
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseDto<Void> delete(
            @LoginId(required = true) String loginId,
            @RequestParam(name = "productid") Long productId
    ) {
        commandWishlistService.delete(loginId, productId);
        return ResponseDto.<Void>builder().status(HttpStatus.NO_CONTENT).success(true).build();
    }
}
