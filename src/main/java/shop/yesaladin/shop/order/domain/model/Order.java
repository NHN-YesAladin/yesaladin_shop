package shop.yesaladin.shop.order.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.persistence.converter.OrderCodeConverter;

/**
 * 주문 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", length = 18, nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDateTime;

    @Column(name = "expected_transport_date", nullable = false)
    private LocalDate expectedTransportDate;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden;

    @Column(name = "used_point", nullable = false)
    private long usedPoint;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "wrapping_fee", nullable = false)
    private int wrappingFee;

    @Column(name = "order_code_id")
    @Convert(converter = OrderCodeConverter.class)
    private OrderCode orderCode;
}
