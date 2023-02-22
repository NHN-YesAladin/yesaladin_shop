package shop.yesaladin.shop.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 배송서버 관련 이벤트 dto, 배송 서버에 결제가 끝난 후 commit이 되고 난 다음 실행시키기 위해 event를 등록
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@AllArgsConstructor
public class DeliveryEventDto {

    private Long orderId;
}
