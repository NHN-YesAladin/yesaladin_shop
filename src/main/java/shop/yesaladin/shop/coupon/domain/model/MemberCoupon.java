package shop.yesaladin.shop.coupon.domain.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 쿠폰 엔터티 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "member_coupons")
public class MemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "coupon_code", nullable = false, unique = true, length = 36)
    private String couponCode;

    @Column(name = "coupon_group_code", nullable = false, length = 36)
    private String couponGroupCode;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false)
    private LocalDate expirationDate;

    public void use() {
        if (this.isUsed) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "Coupon already used. Coupon code : " + this.couponCode);
        }
        this.isUsed = true;
    }
}
