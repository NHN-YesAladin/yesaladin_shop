package shop.yesaladin.shop.payment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 정보를 생성하기 위해 사용하는 요청 Dto from Toss
 *
 * @author 배수한
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotBlank
    private String paymentKey;
    @NotBlank
    private String orderId;
    @NotNull
    private Long amount;
}
