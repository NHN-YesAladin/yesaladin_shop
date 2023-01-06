package shop.yesaladin.shop.payment.domain.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.print.attribute.standard.MediaSize.NA;
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
@Table(name = "payment_cards")
@Entity
public class PaymentCancel {

    @Id
    @Column(name = "payment_id")
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "cancel_amount", nullable = false)
    private long cancelAmount;

    @Column(name = "cancel_reason", nullable = false)
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

    @Column(name = "transaction_key", nullable = false)
    private String transactionKey;
}
