package shop.yesaladin.shop.payment.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.payment.domain.model.Payment;

/**
 * 결제 이벤트 발생 dto
 *  해당 dto가 Application event로 발행이 되면 이벤트 동작의 트리거가 됨
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@AllArgsConstructor
public class PaymentEventDto {

    private String paymentKey;
}
