package shop.yesaladin.shop.payment.dummy;


import java.time.LocalDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

public class DummyPayment {

    public static Payment dummyPayment(String id, Order order) {
        long totalAmount = order.getShippingFee() + order.getWrappingFee();
        return Payment.builder()
                .id(id)
                .lastTransactionKey("DummyLastTransactionKey")
                .orderName(order.getName())
                .method("카드")
                .currency("KRW")
                .totalAmount(totalAmount)
                .balanceAmount(totalAmount)
                .suppliedAmount(totalAmount)
                .taxFreeAmount(0L)
                .vat(0L)
                .status("DONE")
                .requestedDatetime(LocalDateTime.now())
                .approvedDatetime(LocalDateTime.now())
                .order(order)
                .paymentCode(PaymentCode.NORMAL)
                .build();
    }

}
