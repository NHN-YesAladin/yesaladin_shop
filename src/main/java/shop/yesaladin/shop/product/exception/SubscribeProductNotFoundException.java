package shop.yesaladin.shop.product.exception;

/**
 * ID에 맞는 구독상품이 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class SubscribeProductNotFoundException extends RuntimeException {
    public SubscribeProductNotFoundException(Long id) {
        super("SubscribeProduct " + id + "(ID) is not found.");
    }
}
