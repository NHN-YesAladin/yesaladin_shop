package shop.yesaladin.shop.order.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.domain.dummy.MemberAddress;

/**
 * 회원 주문 엔티티입니다.
 *
 * @author 최예린, 송학현
 * @since 1.0
 */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "member_orders")
public class MemberOrder {

    @Id
    @Column(name = "order_id", nullable = false)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_address_id", nullable = false)
    private MemberAddress memberAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @MapsId(value = "orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
