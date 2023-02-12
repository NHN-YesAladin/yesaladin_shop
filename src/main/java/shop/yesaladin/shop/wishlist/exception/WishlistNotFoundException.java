package shop.yesaladin.shop.wishlist.exception;

/**
 * 위시리스트가 삭제 시 없을 경우 예외 처리(404)
 *
 * @author 김선홍
 * @since 1.0
 */
public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(String loginId, Long productId) {
        super(loginId + "의 위시리스트에는 " + productId + "가 위시리스트에 등록되지 않았습니다.");
    }
}
