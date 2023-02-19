package shop.yesaladin.shop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 구독 엔티티 입니다.
 *
 * @author 최예린
 * @author 송학현
 * @since 1.0
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "subscribes")
@PrimaryKeyJoinColumn(name = "order_id")
public class Subscribe extends MemberOrder {

    @Column(name = "expected_day", nullable = false)
    private int expectedDay;

    @Column(name = "interval_month", nullable = false)
    private int intervalMonth;

    @Column(name = "next_renewal_date", nullable = false)
    private LocalDate nextRenewalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_product_id", nullable = false)
    private SubscribeProduct subscribeProduct;
}
