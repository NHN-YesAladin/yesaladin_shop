package shop.yesaladin.shop.payment.domain.repository;

import shop.yesaladin.shop.payment.domain.model.Payment;

/**
 * 결제 정보 생성 및 수정을 위한 레파지토리 인터페이스
 * <p>
 * 결제 카드, 취소 정보들은 Payment에 set을 통해서 insert 가능 (Cascade.ALL 사용)
 *
 * @author 배수한
 * @since 1.0
 */
public interface CommandPaymentRepository {

    Payment save(Payment payment);

    void deleteById(String paymentId);
}
