package shop.yesaladin.shop.payment.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;

/**
 * 결제 정보 조회를 위한 레파지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryPaymentRepository {

    /**
     * 결제 정보 엔티티를 찾기 위한 메서드
     *  Order, PaymentCard, PaymentCancel 과 조인하여 데이터를 불러온다.
     *
     * @param id 동적 쿼리용으로, 찾고자하는 paymentId를 의미한다
     * @param orderId 동적 쿼리용으로, 찾고자하는 orderId를 의미한다
     * @return Optional<Payment>
     */
    Optional<Payment> findById(String id, Long orderId);

    /**
     * 간단하게 화면에 보여줄 결제 정보 엔티티를 찾기위한 메서드
     *   dto projection을 통해 데이터를 select 한다
     *
     * @param id 동적 쿼리용으로, 찾고자하는 paymentId를 의미한다
     * @param orderId orderId 동적 쿼리용으로, 찾고자하는 orderId를 의미한다
     * @return Optional<PaymentCompleteSimpleResponseDto>
     */
    Optional<PaymentCompleteSimpleResponseDto> findSimpleDtoById(String id, Long orderId);

}
