package shop.yesaladin.shop.payment.dummy;


import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentEasyPay;

public class DummyPaymentEasyPay {

    public static PaymentEasyPay paymentEasyPay(Payment payment) {
        return PaymentEasyPay.builder()
                .id(payment.getId())
                .amount(payment.getTotalAmount())
                .payment(payment)
                .discountAmount(0)
                .provider("토스")
                .build();
    }

}
