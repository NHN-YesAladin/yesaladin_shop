package shop.yesaladin.shop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 정기구독 주문의 엔티티입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "subscribe_order_lists")
public class SubscribeOrderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_transported", nullable = false)
    private boolean isTransported;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_order_id", nullable = false)
    private Subscribe subscribe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_order_id")
    private MemberOrder memberOrder;
}
