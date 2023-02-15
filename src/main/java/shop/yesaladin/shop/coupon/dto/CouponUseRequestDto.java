package shop.yesaladin.shop.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUseRequestDto {

    private List<String> couponCodes;
}
