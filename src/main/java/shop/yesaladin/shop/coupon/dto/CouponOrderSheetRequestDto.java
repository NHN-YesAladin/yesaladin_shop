package shop.yesaladin.shop.coupon.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상품에 쿠폰을 적용하기위한 정보를 담은 dto입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponOrderSheetRequestDto {

    private String isbn;
    private int quantity;
    private String couponCode;
    private List<String> duplicateCouponCode;
}
