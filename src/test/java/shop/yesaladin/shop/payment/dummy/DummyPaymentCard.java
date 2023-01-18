package shop.yesaladin.shop.payment.dummy;


import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

public class DummyPaymentCard {

    public static PaymentCard paymentCard(Payment payment) {
        return PaymentCard.builder()
                .payment(payment)
                .id(payment.getId())
                .amount(payment.getTotalAmount())
                .issuerCode("61")
                .acquirerCode("31")
                .number("1111-1111-1111-1111")
                .installmentPlanMonths(0)
                .approveNo("0000011")
                .useCardPoint(false)
                .acquireStatus("READY")
                .isInterestFree(false)
                .interestPayer("NONE")
                .cardCode(PaymentCode.CREDIT)
                .ownerCode(PaymentCode.INDIVIDUAL)
                .build();
    }

}
