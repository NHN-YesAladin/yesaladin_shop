package shop.yesaladin.shop.payment.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 취소 정보 엔터티
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "payment_cancels")
@Entity
public class PaymentCancel {

    @Id
    @Column(name = "payment_id", length = 200)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "cancel_amount", nullable = false)
    private long cancelAmount;

    @Column(name = "cancel_reason", nullable = false, length = 200)
    private String cancelReason;

    @Column(name = "tax_free_amount", nullable = false)
    private long taxFreeAmount;

    @Column(name = "tax_exemption_amount", nullable = false)
    private long taxExemptionAmount;

    @Column(name = "refundable_amount", nullable = false)
    private long refundableAmount;

    @Column(name = "easy_pay_discount_amount", nullable = false)
    private long easyPayDiscountAmount;

    @Column(name = "canceled_datetime", nullable = false)
    private LocalDateTime canceledDatetime;

    @Column(name = "transaction_key", nullable = false, length = 64)
    private String transactionKey;

    public static PaymentCancel toEntity(JsonNode jsonNode) {
        String paymentId = jsonNode.get("paymentKey").asText();
        return PaymentCancel.builder()
                .id(paymentId)
                .cancelAmount(jsonNode.get("cancelAmount").asLong())
                .cancelReason(jsonNode.get("cancelReason").asText())
                .canceledDatetime(ZonedDateTime.parse(jsonNode.get("canceledAt").asText())
                        .withZoneSameInstant(
                                ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .taxFreeAmount(jsonNode.get("taxFreeAmount").asLong())
                .taxExemptionAmount(jsonNode.get("taxExemptionAmount").asLong())
                .refundableAmount(jsonNode.get("refundableAmount").asLong())
                .easyPayDiscountAmount(jsonNode.get("easyPayDiscountAmount").asLong())
                .transactionKey(jsonNode.get("transactionKey").asText())
                .build();
    }
}
