package shop.yesaladin.shop.product.exception;

/**
 * TotalDiscountRate가 존재하지 않는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class TotalDiscountRateNotExistsException extends RuntimeException {

    public TotalDiscountRateNotExistsException() {
        super("TotalDiscountRate is Not Exists.");
    }
}
