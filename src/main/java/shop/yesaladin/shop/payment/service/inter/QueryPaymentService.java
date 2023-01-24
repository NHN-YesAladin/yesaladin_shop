package shop.yesaladin.shop.payment.service.inter;

import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;

/**
 * 결제 정보에 관련한 정보를 조회하기 위한 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryPaymentService {

    /**
     * 주문 id를 통해 결제 정보를 조회하는 서비스
     *
     * @param orderId 찾고자하는 주문 id
     * @return 결제 정보
     */
    PaymentCompleteSimpleResponseDto findByOrderId(Long orderId);
}
