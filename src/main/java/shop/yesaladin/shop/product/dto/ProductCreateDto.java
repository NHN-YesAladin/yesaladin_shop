package shop.yesaladin.shop.product.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 상품 등록을 위한 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
public class ProductCreateDto {

    @NotBlank
    @Length(max = 50)
    private String ISBN;

    // 상품 설명
    @NotBlank
    @Length(max = 255)
    private String title;
    @NotBlank
    private String contents;
    @NotBlank
    private String description;

    // 저자, 출판사
    @NotBlank
    private String Writer;
    @NotBlank
    private String publisher;

    // 정가, 할인율, 포인트 적립율
    @PositiveOrZero
    private long actualPrice;
    @PositiveOrZero
    @Max(100)
    private int discountRate;
    private Boolean isSeparatelyDiscount;
    @PositiveOrZero
    @Max(100)
    private int givenPointRate;
    private Boolean isGivenPoint;

    // 구독 상품 관련
    @NotBlank
    @Length(max = 9)
    private String ISSN;
    private Boolean isSubscriptionAvailable;

    // 판매 여부, 강제품절 여부
    private Boolean isSale;
    private Boolean isForcedOutOfStock;

    // 수량, 출간일, 노출우선순위, 썸네일 UUID
    @PositiveOrZero
    private long quantity;
    @NotBlank
    private String publishedDate;
    @PositiveOrZero
    private int preferentialShowRanking;
    @NotBlank
    private String uuid;

    // codes
    @NotBlank
    private String productTypeCode;
    @NotBlank
    private String productSavingMethodCode;
}
