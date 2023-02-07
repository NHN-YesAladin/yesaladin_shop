package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.OrderRecipient;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRecipientRepository;

/**
 * 수령인 관련 jpa repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaOrderRecipientRepository extends Repository<OrderRecipient, Long>,
        CommandOrderRecipientRepository {

}
