package shop.yesaladin.shop.coupon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.code.TriggerTypeCode;

/**
 * 쿠폰 지급 요청시 사용하는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponGiveRequestDto {

    private TriggerTypeCode triggerTypeCode;
    private Long couponId;
}
