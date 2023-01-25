package shop.yesaladin.shop.payment.dummy;


import java.time.LocalDateTime;
import java.util.UUID;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCancel;

public class DummyPaymentCancel {

    public static PaymentCancel paymentCancel(Payment payment) {
        return PaymentCancel.builder()
                .payment(payment)
                .id(payment.getId())
                .cancelAmount(payment.getTotalAmount())
                .cancelReason("변심")
                .taxFreeAmount(0L)
                .taxExemptionAmount(0L)
                .refundableAmount(0L)
                .easyPayDiscountAmount(0L)
                .canceledDatetime(LocalDateTime.now())
                .transactionKey(UUID.randomUUID().toString())
                .build();
    }
}
