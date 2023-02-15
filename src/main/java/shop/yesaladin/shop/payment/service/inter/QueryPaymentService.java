package shop.yesaladin.shop.payment.service.inter;

import shop.yesaladin.shop.payment.domain.model.Payment;

/**
 * 결제 정보에 관련한 정보를 조회하기 위한 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryPaymentService {


    /**
     * 주문 id를  통해 결제 정보 조회
     *
     * @param orderId 주문 id
     * @return
     */
    Payment findByOrderId(long orderId);

}
