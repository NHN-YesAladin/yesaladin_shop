package shop.yesaladin.shop.payment.exception;

import lombok.Getter;

/**
 * 토스로 부터 실패했을 경우 발생하는 예외
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
public class PaymentFailException extends RuntimeException {

    private String code;
    private String message;

    public PaymentFailException(String message, String code) {
        super("Payment is fail : " + message);
        this.message = message;
        this.code = code;
    }
}
