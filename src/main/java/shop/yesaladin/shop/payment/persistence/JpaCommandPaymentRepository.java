package shop.yesaladin.shop.payment.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;

/**
 * @author 배수한
 * @since 1.0
 */
public interface JpaCommandPaymentRepository extends Repository<Payment, String>,
        CommandPaymentRepository {

}
