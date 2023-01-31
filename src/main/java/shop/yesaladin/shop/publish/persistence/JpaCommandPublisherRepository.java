package shop.yesaladin.shop.publish.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublisherRepository;

/**
 * 출판사 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaCommandPublisherRepository extends Repository<Publisher, Long>,
        CommandPublisherRepository {

}
