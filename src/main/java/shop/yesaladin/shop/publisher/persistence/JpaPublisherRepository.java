package shop.yesaladin.shop.publisher.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.publisher.domain.model.Publisher;
import shop.yesaladin.shop.publisher.domain.repository.CommandPublisherRepository;

public interface JpaPublisherRepository extends Repository<Publisher, Long>,
        CommandPublisherRepository {

}
