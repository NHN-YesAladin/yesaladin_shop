package shop.yesaladin.shop.product.exception;

/**
 * 이미 삭제된(soft delete) 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class AlreadyDeletedProductException extends RuntimeException {

    public AlreadyDeletedProductException(Long id) {
        super("Product " + id + "(ID) is already deleted.");
    }
}
