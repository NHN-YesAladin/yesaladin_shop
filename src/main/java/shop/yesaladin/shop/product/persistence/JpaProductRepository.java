package shop.yesaladin.shop.product.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;

/**
 * 상품 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaProductRepository extends Repository<Product, Long>,
        CommandProductRepository {

}
