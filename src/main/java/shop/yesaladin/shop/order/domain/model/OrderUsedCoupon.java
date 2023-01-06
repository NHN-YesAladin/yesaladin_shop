package shop.yesaladin.shop.order.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.dummy.CouponIssuance;

/**
 * 주문에 사용한 쿠폰 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "order_used_coupons")
public class OrderUsedCoupon {

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @MapsId(value = "orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId(value = "couponIssuanceId")
    @JoinColumn(name = "coupon_issuance_id", nullable = false)
    private CouponIssuance couponIssuance;

    /**
     * 주문에 사용한 쿠폰의 복합키입니다.
     *
     * @author 최예린
     * @since 1.0
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "order_id", nullable = false)
        private long orderId;

        @Column(name = "coupon_issuance_id", nullable = false)
        private long couponIssuanceId;
    }
}
