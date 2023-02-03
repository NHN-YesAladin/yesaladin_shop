package shop.yesaladin.shop.product.exception;

/**
 * 이미 존재하는 ISBN인 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String isbn) {
        super("Product " + isbn + "(ISBN) is already exists.");
    }
}
