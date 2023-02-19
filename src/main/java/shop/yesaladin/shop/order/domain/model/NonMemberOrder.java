package shop.yesaladin.shop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 비회원 주문 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "non_member_orders")
@PrimaryKeyJoinColumn(name = "order_id")
public class NonMemberOrder extends Order {

    @Column(nullable = false)
    private String address;

    @Column(name = "name", length = 20, nullable = false)
    private String nonMemberName;

    @Column(name = "phone_number", length = 13, nullable = false)
    private String phoneNumber;
}
