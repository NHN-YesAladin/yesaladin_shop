package shop.yesaladin.shop.order.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
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

/**
 * 정기구독 주문의 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "subscribe_orders")
public class SubscribeOrder {

    @EmbeddedId
    private Pk pk;

    @Column(name = "is_transported", nullable = false)
    private boolean isTransported;

    @Column(name = "expected_date", nullable = false)
    private LocalDate expectedDate;

    @MapsId(value = "orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @MapsId(value = "subscribeId")
    @ManyToOne
    @JoinColumn(name = "subscribe_id", nullable = false)
    private Subscribe subscribe;

    /**
     * 정기구독 주문의 복합키입니다.
     *
     * @author 최예린
     * @since 1.0
     */
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "order_id", nullable = false)
        private long orderId;

        @Column(name = "subscribe_id", nullable = false)
        private long subscribeId;
    }
}
