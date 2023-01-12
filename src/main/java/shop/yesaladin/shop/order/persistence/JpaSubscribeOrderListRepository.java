package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.domain.repository.CommandSubscribeOrderListRepository;
import shop.yesaladin.shop.order.domain.repository.QuerySubscribeOrderListRepository;

/**
 * 정기구독내역 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaSubscribeOrderListRepository extends Repository<SubscribeOrderList, Long>,
        CommandSubscribeOrderListRepository, QuerySubscribeOrderListRepository {

}
