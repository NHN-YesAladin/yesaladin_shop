package shop.yesaladin.shop.coupon.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponGroupCodesAndLimitDto {

    private List<String> couponCodes;
    private Boolean isLimited;
}
