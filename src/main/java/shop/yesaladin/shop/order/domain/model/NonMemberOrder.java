package shop.yesaladin.shop.order.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비회원 주문 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "non_member_orders")
public class NonMemberOrder {

    @Id
    @Column(name = "order_id")
    private long orderId;

    @Column(nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 13, nullable = false)
    private String phoneNumber;

    @MapsId(value = "orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
