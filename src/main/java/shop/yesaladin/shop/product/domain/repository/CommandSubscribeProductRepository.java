package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 구독상품 등록 및 수정 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandSubscribeProductRepository {

    SubscribeProduct save(SubscribeProduct subscribeProduct);
}
