package shop.yesaladin.shop.order.domain.model;

import lombok.*;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;

import javax.persistence.*;
import java.io.Serializable;

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
@Table(name = "order_coupons")
public class OrderCoupon {

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
     * @param memberOrder  회원주문 데이터
     * @param memberCoupon 회원이 소유한 쿠폰
     * @return 주문에 사용한 쿠폰 엔티티
     * @author 최예린
     * @since 1.0
     */
    public static OrderCoupon create(MemberOrder memberOrder, MemberCoupon memberCoupon) {
        Pk pk = new Pk(memberOrder.getId(), memberCoupon.getId());
        return new OrderCoupon(pk, memberOrder, memberCoupon);
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
