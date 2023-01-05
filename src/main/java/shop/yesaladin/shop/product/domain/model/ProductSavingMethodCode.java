package shop.yesaladin.shop.product.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 적립방식 코드의 enum 클래스입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum ProductSavingMethodCode {
    ACTUAL_PURCHASE_PRICE(1),
    SELLING_PRICE(2);

    private final int id;
}
