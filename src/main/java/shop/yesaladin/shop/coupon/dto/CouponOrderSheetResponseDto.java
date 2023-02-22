package shop.yesaladin.shop.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품에 쿠폰을 적용한 결과값을 반환하는 dto 입니다
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponOrderSheetResponseDto {

    private String isbn;
    private List<String> memberCoupons;
    private List<String> memberCouponNames;
    private long discountPrice;
    private long expectedPoint;
}
