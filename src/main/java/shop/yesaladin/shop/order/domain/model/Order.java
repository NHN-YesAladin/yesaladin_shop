package shop.yesaladin.shop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import shop.yesaladin.shop.order.persistence.converter.OrderCodeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 주문 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Order {

    @Column(name = "is_hidden", nullable = false)
    protected boolean isHidden;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "order_number", length = 18, nullable = false, unique = true)
    private String orderNumber;
    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDateTime;
    @Column(name = "expected_transport_date")
    private LocalDate expectedTransportDate;
    @Column(name = "used_point", nullable = false)
    private long usedPoint;
    @Column(name = "saved_point", nullable = false)
    private long savedPoint;
    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "wrapping_fee", nullable = false)
    private int wrappingFee;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Column(name = "recipient_name", nullable = false, length = 20)
    private String recipientName;

    @Column(name = "recipient_phone_number", nullable = false, length = 11)
    private String recipientPhoneNumber;

    @Column(name = "order_code_id", nullable = false)
    @Convert(converter = OrderCodeConverter.class)
    private OrderCode orderCode;
}
