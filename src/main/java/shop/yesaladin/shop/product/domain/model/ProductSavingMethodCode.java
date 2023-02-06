package shop.yesaladin.shop.product.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 적립방식 코드의 Enum 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum ProductSavingMethodCode {
    ACTUAL_PURCHASE_PRICE(1, "실구매가"),
    SELLING_PRICE(2, "판매가");

    private final int id;
    private final String koName;
}
