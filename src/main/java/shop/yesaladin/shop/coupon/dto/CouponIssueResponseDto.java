package shop.yesaladin.shop.coupon.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 발행 후 발행된 쿠폰 코드들을 저장하는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueResponseDto {

    private List<String> createdCouponCodes;
}
