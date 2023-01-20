package shop.yesaladin.shop.payment.service.inter;

import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;

/**
 * 결제 정보, 카드정보, 취소정보를 생성,수정,삭제 할 수 있는 기능을 가진 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface CommandPaymentService {

    PaymentCompleteSimpleResponseDto confirmTossRequest(PaymentRequestDto requestDto);

}
