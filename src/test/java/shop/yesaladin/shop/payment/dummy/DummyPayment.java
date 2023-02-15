package shop.yesaladin.shop.payment.dummy;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

public class DummyPayment {

    public static Payment payment(String id, Order order) {
        long totalAmount = order.getShippingFee() + order.getWrappingFee();
        return Payment.builder()
                .id(id)
                .lastTransactionKey("DummyLastTransactionKey")
                .orderName(order.getName())
                .method(PaymentCode.CARD)
                .currency(Payment.CURRENCY_KRW)
                .totalAmount(totalAmount)
                .balanceAmount(totalAmount)
                .suppliedAmount(totalAmount)
                .taxFreeAmount(0L)
                .vat(0L)
                .status(PaymentCode.DONE)
                .requestedDatetime(ZonedDateTime.now().withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .approvedDatetime(ZonedDateTime.now().withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .order(order)
                .paymentCode(PaymentCode.NORMAL)
                .build();
    }

    public static Payment payment(String id, Order order, PaymentCode paymentCode) {
        long totalAmount = order.getShippingFee() + order.getWrappingFee();
        return Payment.builder()
                .id(id)
                .lastTransactionKey("DummyLastTransactionKey")
                .orderName(order.getName())
                .method(paymentCode)
                .currency(Payment.CURRENCY_KRW)
                .totalAmount(totalAmount)
                .balanceAmount(totalAmount)
                .suppliedAmount(totalAmount)
                .taxFreeAmount(0L)
                .vat(0L)
                .status(PaymentCode.DONE)
                .requestedDatetime(ZonedDateTime.now().withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .approvedDatetime(ZonedDateTime.now().withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .order(order)
                .paymentCode(PaymentCode.NORMAL)
                .build();
    }
}
