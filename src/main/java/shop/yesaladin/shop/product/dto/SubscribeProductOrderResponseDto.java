package shop.yesaladin.shop.product.dto;

import lombok.Getter;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 주문에 사용할 정기구독 상품의 데이터를 가져오기 위한 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
public class SubscribeProductOrderResponseDto {

    private final String title;
    private final SubscribeProduct subscribeProduct;

    public SubscribeProductOrderResponseDto(Product product) {
        this.title = product.getTitle();
        this.subscribeProduct = product.getSubscribeProduct();
    }
}
