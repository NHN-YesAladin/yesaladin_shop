package shop.yesaladin.shop.payment.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.payment.persistence.converter.PaymentCodeConverter;

/**
 * 결제정보 엔터티
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "payments")
@Entity
public class Payment {

    public static final String CURRENCY_KRW = "KRW";
    private static final String EASY_PAY = "easyPay";
    private static final String CARD = "card";
    @Id
    @Column(length = 200)
    private String id;

    @Column(name = "last_transaction_key", length = 64)
    private String lastTransactionKey;

    @Column(name = "order_name", nullable = false, length = 100)
    private String orderName;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Column(name = "balance_amount", nullable = false)
    private long balanceAmount;

    @Column(name = "supplied_amount", nullable = false)
    private long suppliedAmount;

    @Column(name = "tax_free_amount", nullable = false)
    private long taxFreeAmount;

    @Column(nullable = false)
    private long vat;

    @Column(name = "requested_datetime", nullable = false)
    private LocalDateTime requestedDatetime;

    @Column(name = "approved_datetime", nullable = false)
    private LocalDateTime approvedDatetime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_code_id")
    @Convert(converter = PaymentCodeConverter.class)
    private PaymentCode paymentCode;

    @Column(name = "method_code_id")
    @Convert(converter = PaymentCodeConverter.class)
    private PaymentCode method;

    @Column(name = "status_code_id")
    @Convert(converter = PaymentCodeConverter.class)
    private PaymentCode status;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private PaymentCard paymentCard;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private PaymentCancel paymentCancel;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private PaymentEasyPay paymentEasyPay;

    public static Payment toEntity(JsonNode jsonNode, Order order) {
        String paymentId = jsonNode.get("paymentKey").asText();

        PaymentCode paymentMethod = PaymentCode.findByName(jsonNode.get("method").asText());
        Payment payment = Payment.builder()
                .id(paymentId)
                .lastTransactionKey(jsonNode.get("lastTransactionKey").asText())
                .orderName(jsonNode.get("orderName").asText())
                .currency(jsonNode.get("currency").asText())
                .totalAmount(jsonNode.get("totalAmount").asLong())
                .balanceAmount(jsonNode.get("balanceAmount").asLong())
                .suppliedAmount(jsonNode.get("suppliedAmount").asLong())
                .taxFreeAmount(jsonNode.get("taxFreeAmount").asLong())
                .vat(jsonNode.get("vat").asLong())
                .requestedDatetime(ZonedDateTime.parse(jsonNode.get("requestedAt").asText())
                        .withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .approvedDatetime(ZonedDateTime.parse(jsonNode.get("approvedAt").asText())
                        .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .order(order)
                .paymentCode(PaymentCode.valueOf(jsonNode.get("type").asText()))
                .method(paymentMethod)
                .status(PaymentCode.valueOf(jsonNode.get("status").asText()))
                .build();
        if (paymentMethod.equals(PaymentCode.EASY_PAY)) {
            PaymentEasyPay easyPay = PaymentEasyPay.builder()
                    .id(paymentId)
                    .payment(payment)
                    .provider(jsonNode.get(EASY_PAY).get("provider").asText())
                    .discountAmount(jsonNode.get(EASY_PAY).get("discountAmount").asLong())
                    .amount(jsonNode.get(EASY_PAY).get("amount").asLong())
                    .build();
            payment.setPaymentEasyPay(easyPay);
            return payment;
        }
        PaymentCard paymentCard = PaymentCard.builder()
                .id(paymentId)
                .payment(payment)
                .amount(jsonNode.get(CARD).get("amount").asLong())
                .number(jsonNode.get(CARD).get("number").asText())
                .installmentPlanMonths(jsonNode.get(CARD).get("installmentPlanMonths").asInt())
                .approveNo(jsonNode.get(CARD).get("approveNo").asText())
                .useCardPoint(jsonNode.get(CARD).get("useCardPoint").asBoolean())
                .isInterestFree(jsonNode.get(CARD).get("isInterestFree").asBoolean())
                .interestPayer(jsonNode.get(CARD).get("interestPayer").asText())
                .cardCode(PaymentCode.findByName(jsonNode.get(CARD).get("cardType").asText()))
                .ownerCode(PaymentCode.findByName(jsonNode.get(CARD).get("ownerType").asText()))
                .acquireStatus(PaymentCode.valueOf(jsonNode.get(CARD)
                        .get("acquireStatus")
                        .asText()))
                .issuerCode(PaymentCardAcquirerCode.findByName(jsonNode.get(CARD)
                        .get("issuerCode")
                        .asText()))
                .acquirerCode(PaymentCardAcquirerCode.findByName(jsonNode.get(CARD)
                        .get("acquirerCode")
                        .asText()))
                .build();
        payment.setPaymentCard(paymentCard);
        return payment;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public void setPaymentCancel(PaymentCancel paymentCancel) {
        this.paymentCancel = paymentCancel;
    }

    public void setPaymentEasyPay(PaymentEasyPay paymentEasyPay) {
        this.paymentEasyPay = paymentEasyPay;
    }

    public void setStatus(PaymentCode status) {
        this.status = status;
    }
}

