package shop.yesaladin.shop.payment.exception;

/**
 * 토스로 부터 실패했을 경우 발생하는 예외
 *
 * @author 배수한
 * @since 1.0
 */
public class PaymentFailException extends RuntimeException{

    public PaymentFailException(Object msg) {
        super("Payment is fail : " + msg);
    }
}
