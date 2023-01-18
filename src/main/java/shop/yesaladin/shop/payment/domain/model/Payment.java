package shop.yesaladin.shop.payment.domain.model;

import java.time.LocalDateTime;
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
import lombok.ToString;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.payment.persistence.converter.PaymentCodeConverter;

/**
 * 결제정보 엔터티
 *
 * @author 서민지
 * @since 1.0
 */
@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @Column(length = 200)
    private String id;

    @Column(name = "last_transaction_key", nullable = false, length = 64)
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

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PaymentCard paymentCard;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PaymentCancel paymentCancel;

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public void setPaymentCancel(PaymentCancel paymentCancel) {
        this.paymentCancel = paymentCancel;
    }

    public void setStatus(PaymentCode status) {
        this.status = status;
    }
}
