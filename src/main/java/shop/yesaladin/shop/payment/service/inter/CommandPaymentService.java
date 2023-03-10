package shop.yesaladin.shop.payment.service.inter;

import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;

/**
 * 결제 정보, 카드정보, 취소정보를 생성,수정,삭제 할 수 있는 기능을 가진 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface CommandPaymentService {

    /**
     * 토스로부터 결제 승인을 받기 위한 메서드
     *
     * @param requestDto 필수 결제 정보가 포함되어있는 dto
     * @return 결제 정보
     */
    PaymentCompleteSimpleResponseDto confirmTossRequest(PaymentRequestDto requestDto);

    /**
     * 결제 취소 메서드
     *
     * @param paymentKey 결제 취소를 위한 key 값
     */
    void cancelPayment(String paymentKey, String cancelReason);

}
