package shop.yesaladin.shop.order.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import shop.yesaladin.shop.order.persistence.converter.OrderCodeConverter;

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

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "wrapping_fee", nullable = false)
    private int wrappingFee;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Column(name = "order_code_id", nullable = false)
    @Convert(converter = OrderCodeConverter.class)
    private OrderCode orderCode;

    @JoinColumn(name = "order_recipient_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderRecipient orderRecipient;
}
