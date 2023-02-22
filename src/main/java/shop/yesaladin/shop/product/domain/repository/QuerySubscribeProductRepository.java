package shop.yesaladin.shop.product.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 구독상품 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QuerySubscribeProductRepository {

    Optional<SubscribeProduct> findById(Long id);

    Optional<SubscribeProduct> findByISSN(String ISSN);
}
