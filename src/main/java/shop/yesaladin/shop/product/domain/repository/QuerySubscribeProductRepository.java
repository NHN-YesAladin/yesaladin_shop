package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

import java.util.Optional;

/**
 * 구독상품 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QuerySubscribeProductRepository {

    Optional<SubscribeProduct> findByISSN(String ISSN);
}
