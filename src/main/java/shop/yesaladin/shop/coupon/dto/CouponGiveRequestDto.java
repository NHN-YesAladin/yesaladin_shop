package shop.yesaladin.shop.coupon.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    @DateTimeFormat(pattern = "yy- MM-dd HH:mm")
    private LocalDateTime requestDateTime;
}
