package shop.yesaladin.shop.coupon.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;

@Getter
@Builder
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private String name;
    private String couponCode;
    private long amount;
    private CouponTypeCode couponTypeCode;
    private LocalDateTime expireDate;
    private Boolean isUsed;
    private String couponBound;
    private CouponBoundCode couponBoundCode;
}
