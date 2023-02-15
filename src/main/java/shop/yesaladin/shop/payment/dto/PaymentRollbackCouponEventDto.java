package shop.yesaladin.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 결제 롤백시, 쿠폰 발행 이벤트 발생 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@AllArgsConstructor
public class PaymentRollbackCouponEventDto {

    private String orderNumber;
}
