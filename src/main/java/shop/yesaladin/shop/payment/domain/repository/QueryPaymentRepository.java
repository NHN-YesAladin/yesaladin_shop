package shop.yesaladin.shop.payment.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.payment.domain.model.Payment;

/**
 * 결제 정보 조회를 위한 레파지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryPaymentRepository {

    Optional<Payment> findById(String id);
    Optional<Payment> findById(Long orderId);
}
