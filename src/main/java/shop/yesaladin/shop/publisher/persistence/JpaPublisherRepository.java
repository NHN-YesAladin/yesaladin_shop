package shop.yesaladin.shop.publisher.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.publisher.domain.model.Publisher;
import shop.yesaladin.shop.publisher.domain.repository.CommandPublisherRepository;

/**
 * 춣판사 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaPublisherRepository extends Repository<Publisher, Long>,
        CommandPublisherRepository {

}
