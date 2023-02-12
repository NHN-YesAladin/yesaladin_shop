package shop.yesaladin.shop.coupon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.coupon.code.TriggerTypeCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponGiveRequestDto {

    private TriggerTypeCode triggerTypeCode;
    private Long couponId;
}
