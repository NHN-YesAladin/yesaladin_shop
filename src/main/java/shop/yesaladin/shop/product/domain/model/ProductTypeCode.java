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
    NONE(1, "없음"),
    BESTSELLER(2, "베스트셀러"),
    RECOMMENDATION(3, "추천"),
    NEWBOOK(4, "신간"),
    POPULARITY(5, "인기"),
    DISCOUNTS(6, "할인");

    private final int id;
    private final String koName;
}
