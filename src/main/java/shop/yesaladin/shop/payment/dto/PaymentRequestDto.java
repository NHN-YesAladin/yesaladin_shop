package shop.yesaladin.shop.payment.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.yesaladin.shop.payment.domain.model.PaymentCardAcquirerCode;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

/**
 * 결제 정보를 생성하기 위해 사용하는 요청 dto
 *
 * @author 배수한
 * @since 1.0
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequestDto {
    private PaymentCode cardCode;
    private PaymentCardAcquirerCode cardAcquirerCode;
}
