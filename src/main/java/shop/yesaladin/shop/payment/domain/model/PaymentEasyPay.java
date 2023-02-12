package shop.yesaladin.shop.payment.domain.model;

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
 * 간편결제 정보 엔터티
 *
 * @author 배수한
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "payment_easy_pays")
@Entity
public class PaymentEasyPay {

    @Id
    @Column(name = "payment_id", length = 200)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(nullable = false)
    private long amount;

    @Column(name = "discount_amount", nullable = false)
    private long discountAmount;

    @Column(name = "provider", length = 50, nullable = false)
    private String provider;

}
