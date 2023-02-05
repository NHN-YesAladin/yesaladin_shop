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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

/**
 * 주문에 사용한 쿠폰 엔티티입니다.
 *
 * @author 최예린
 * @author 서민지
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "order_used_coupons")
public class OrderUsedCoupon {

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @MapsId(value = "memberOrderId")
    @JoinColumn(name = "member_order_id", nullable = false)
    private MemberOrder memberOrder;

    @ManyToOne
    @MapsId(value = "memberCouponId")
    @JoinColumn(name = "member_coupon_id", nullable = false)
    private MemberCoupon memberCoupon;

    /**
     * 주문에 사용한 쿠폰 엔티티 생성 메소드입니다.
     *
     * @param memberOrder 회원주문 데이터
     * @param memberCoupon 회원이 소유한 쿠폰
     * @return 주문에 사용한 쿠폰 엔티티
     * @author 최예린
     * @since 1.0
     */
    public static OrderUsedCoupon create(MemberOrder memberOrder, MemberCoupon memberCoupon) {
        Pk pk = new Pk(memberOrder.getId(), memberCoupon.getId());
        return new OrderUsedCoupon(pk, memberOrder, memberCoupon);
    }

    /**
     * 주문에 사용한 쿠폰의 복합키입니다.
     *
     * @author 최예린
     * @since 1.0
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "member_order_id", nullable = false)
        private long memberOrderId;

        @Column(name = "member_coupon_id", nullable = false)
        private long memberCouponId;
    }
}
