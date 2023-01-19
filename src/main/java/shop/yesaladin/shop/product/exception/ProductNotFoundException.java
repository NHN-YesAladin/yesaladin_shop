package shop.yesaladin.shop.product.exception;

/**
 * ID에 맞는 상품이 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product " + id + "(ID) is not found.");
    }
}
