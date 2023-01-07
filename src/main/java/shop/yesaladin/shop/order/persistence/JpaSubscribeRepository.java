package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.repository.CommandSubscribeRepository;
import shop.yesaladin.shop.order.domain.repository.QuerySubscribeRepository;

/**
 * 정기구독 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaSubscribeRepository extends Repository<Subscribe, Long>,
        CommandSubscribeRepository, QuerySubscribeRepository {

}
