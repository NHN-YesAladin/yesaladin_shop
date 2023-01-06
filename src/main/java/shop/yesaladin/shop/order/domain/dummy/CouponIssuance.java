package shop.yesaladin.shop.order.domain.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 연관관계 매핑을 위한 dummy CouponIssuance 엔티티입니다.
 *
 * @author 최예린
 */
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssuance {
    @Id
    private int id;

}
