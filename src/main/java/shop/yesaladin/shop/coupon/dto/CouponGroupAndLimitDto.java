package shop.yesaladin.shop.coupon.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 서버에서 쿠폰 그룹 코드와 쿠폰 무제한 여부를 가져오기 위한 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponGroupAndLimitDto {

    private String couponGroupCode;
    private Boolean isLimited;
}
