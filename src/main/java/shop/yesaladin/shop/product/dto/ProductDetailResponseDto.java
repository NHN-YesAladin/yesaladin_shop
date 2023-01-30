package shop.yesaladin.shop.product.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 싱세 정보를 응답하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {

    private Long id;

    private String ebookFileUrl;
    private String title;
    private List<String> authors;
    private String publisher;

    private String thumbnailFileUrl;

    private long actualPrice;
    private long sellingPrice;
    private int discountRate;
    private long pointPrice;
    private int pointRate;

    private String publishedDate;
    private String isbn;
    private boolean isSubscriptionAvailable;
    private String issn;

    private String contents;

    private String description;

    // 판매여부 관련
    private long quantity;
    private boolean isForcedOutOfStock;
    private boolean isSale;
    private boolean isDeleted;
}
