package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.Subscribe;

public interface QuerySubscribeRepository {
    Optional<Subscribe> findById(Long id);
}
