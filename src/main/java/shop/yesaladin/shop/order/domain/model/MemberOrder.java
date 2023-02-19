package shop.yesaladin.shop.order.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

import javax.persistence.*;

/**
 * 회원 주문 엔티티입니다.
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
@Table(name = "member_orders")
@PrimaryKeyJoinColumn(name = "order_id")
public class MemberOrder extends Order {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_address_id", nullable = false)
    private MemberAddress memberAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 주문을 숨김처리 합니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void hiddenOn() {
        if (this.isHidden) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Order already hidden with id :" + getId()
            );
        }
        this.isHidden = true;
    }

    /**
     * 주문의 숨김처리를 해제 합니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void hiddenOff() {
        if (!this.isHidden) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Order already showing with id :" + getId()
            );
        }
        this.isHidden = false;
    }
}
