package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderRecipient;

/**
 * 수령인 등록 관련 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderRecipientRepository {

    OrderRecipient save(OrderRecipient orderRecipient);
}
