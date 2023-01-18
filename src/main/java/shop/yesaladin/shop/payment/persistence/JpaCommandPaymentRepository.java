package shop.yesaladin.shop.payment.persistence;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;

/**
 * JPA를 활용하여 Payments 테이블에 접근하여 결제 정보를 생성, 수정 하는 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface JpaCommandPaymentRepository extends Repository<Payment, String>,
        CommandPaymentRepository {

}
