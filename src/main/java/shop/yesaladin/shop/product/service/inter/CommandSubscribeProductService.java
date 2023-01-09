package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 구독상품 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandSubscribeProductService {

    SubscribeProduct register(SubscribeProduct subscribeProduct);
}
