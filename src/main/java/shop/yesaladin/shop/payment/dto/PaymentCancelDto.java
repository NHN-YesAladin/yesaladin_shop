package shop.yesaladin.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 결제 취소를 위한 body용 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelDto {

    private String cancelReason;

}
