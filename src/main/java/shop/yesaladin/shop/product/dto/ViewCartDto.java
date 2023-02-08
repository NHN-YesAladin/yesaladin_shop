package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장바구니에 담은 상품에 대한 정보를 담은 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewCartDto {

    private Long id;
    private Integer quantity;
    private String isbn;

    private String thumbnailFileUrl;
    private String title;

    private long actualPrice;
    private long sellingPrice;
    private int discountRate;
    private long pointPrice;

    // 강제 품절 || 품절 -> 노출 + 품절 표시 + 주문 불가
    private Boolean isOutOfStack;
    // 판매 여부 -> 노출 + 주문 불가
    private Boolean isSale;
    // 삭제 여부 -> 노출 금지
    private Boolean isDeleted;

    private Boolean isEbook;
    private Boolean isSubscribeProduct;
}
