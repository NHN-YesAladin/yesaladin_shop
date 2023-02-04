package shop.yesaladin.shop.coupon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponGroupAndLimitDto {

    private String couponGroupCode;
    private Boolean isLimited;
}
