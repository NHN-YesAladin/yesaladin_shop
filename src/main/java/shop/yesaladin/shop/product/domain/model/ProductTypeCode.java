package shop.yesaladin.shop.product.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 상품 유형 코드의 enum 클래스압나다.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum ProductTypeCode {
    BESTSELLER(1),
    RECOMMENDATION(2),
    NEWBOOK(3),
    POPULARITY(4),
    DISCOUNTS(5),
    NONE(6);

    private final int id;
}
