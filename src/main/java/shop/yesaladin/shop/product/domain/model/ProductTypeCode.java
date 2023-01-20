package shop.yesaladin.shop.product.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 상품 유형 코드의 enum 클래스압나다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum ProductTypeCode {
    NONE(1),
    BESTSELLER(2),
    RECOMMENDATION(3),
    NEWBOOK(4),
    POPULARITY(5),
    DISCOUNTS(6);

    private final int id;
}
