package shop.yesaladin.shop.product.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.CommandSubscribeProductRepository;
import shop.yesaladin.shop.product.domain.repository.QuerySubscribeProductRepository;

/**
 * 구독상품 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaSubscribeProductRepository extends Repository<SubscribeProduct, Long>,
        CommandSubscribeProductRepository, QuerySubscribeProductRepository {

}
