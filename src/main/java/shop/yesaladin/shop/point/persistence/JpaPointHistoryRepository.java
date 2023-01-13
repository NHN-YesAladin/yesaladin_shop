package shop.yesaladin.shop.point.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;

/**
 * 포인트내역 엔티티를 위한 Jpa Repository 인터페이스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaPointHistoryRepository extends Repository<PointHistory, Long>,
        CommandPointHistoryRepository, QueryPointHistoryRepository {

}
