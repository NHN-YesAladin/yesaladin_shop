package shop.yesaladin.shop.publish.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publish.Pk;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;

/**
 * 출판 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaPublishRepository extends Repository<Publish, Pk>,
        CommandPublishRepository, QueryPublishRepository {

}
