package shop.yesaladin.shop.payment.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import shop.yesaladin.shop.payment.persistence.converter.PaymentCodeConverter;

/**
 * 결제 카드 정보 엔터티
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
public class PaymentCard {

    @Id
    @Column(name = "payment_id", length = 200)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(nullable = false)
    private long amount;

    @Column(name = "issuer_code", nullable = false, length = 50)
    private String issuerCode;

    @Column(name = "acquirer_code", nullable = false, length = 50)
    private String acquirerCode;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(name = "installment_plan_months", nullable = false)
    private int installmentPlanMonths;

    @Column(name = "approve_no", nullable = false, length = 8)
    private String approveNo;

    @Column(name = "use_card_point", nullable = false)
    private boolean useCardPoint;

    @Column(name = "acquire_status", nullable = false, length = 20)
    private String acquireStatus;

    @Column(name = "is_interest_free", nullable = false)
    private boolean isInterestFree;

    @Column(name = "interest_payer", nullable = false, length = 20)
    private String interestPayer;

    @Column(name = "card_code_id")
    @Convert(converter = PaymentCodeConverter.class)
    private PaymentCode cardCode;

    @Column(name = "owner_code_id")
    @Convert(converter = PaymentCodeConverter.class)
    private PaymentCode ownerCode;
}
