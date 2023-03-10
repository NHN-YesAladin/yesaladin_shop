package shop.yesaladin.shop.order.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import shop.yesaladin.shop.order.persistence.converter.OrderStatusCodeConverter;

/**
 * 주문 상태 변경 이력 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_status_change_logs")
public class OrderStatusChangeLog {

    @EmbeddedId
    private Pk pk;

    @Column(name = "order_status_code_id")
    @Convert(converter = OrderStatusCodeConverter.class)
    private OrderStatusCode orderStatusCode;

    @MapsId(value = "orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * 주문상태변경내역 엔티티 생성 메소드입니다.
     *
     * @param order           주문 데이터
     * @param changeDateTime  변경된 일시
     * @param orderStatusCode 주문 상태 코드
     * @return 주문상태변경내역 엔티티
     * @author 최예린
     * @since 1.0
     */
    public static OrderStatusChangeLog create(
            Order order,
            LocalDateTime changeDateTime,
            OrderStatusCode orderStatusCode
    ) {
        Pk pk = new Pk(order.getId(), changeDateTime);
        return new OrderStatusChangeLog(pk, orderStatusCode, order);
    }

    /**
     * 주문 상태 변경 이력의 복합키입니다.
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
        @Column(name = "change_datetime", nullable = false)
        private LocalDateTime changeDateTime;
    }
}

