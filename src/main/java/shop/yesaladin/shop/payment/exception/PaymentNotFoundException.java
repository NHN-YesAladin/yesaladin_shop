package shop.yesaladin.shop.payment.exception;

/**
 * @author 배수한
 * @since 1.0
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(Object msg) {
        super("Payment not found : " + msg);
    }
}
