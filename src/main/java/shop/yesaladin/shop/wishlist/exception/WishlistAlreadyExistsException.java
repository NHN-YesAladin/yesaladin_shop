package shop.yesaladin.shop.wishlist.exception;

/**
 * 위시리스트가 추가 시 이미 있을 경우 경우 예외 처리(400)
 *
 * @author 김선홍
 * @since 1.0
 */
public class WishlistAlreadyExistsException extends RuntimeException {

    public WishlistAlreadyExistsException(Long memberId, Long productId) {
        super(memberId+ "은(는) " + productId + "를 이미 위시리스트에 등록하였습니다.");
    }
}
