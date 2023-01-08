package shop.yesaladin.shop.product.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductRepository {

    Optional<Product> findById(Long id);
}
