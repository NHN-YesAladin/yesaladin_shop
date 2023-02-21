package shop.yesaladin.shop.product.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

/**
 * 상품 등록을 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {

    @NotBlank
    @Length(min = 13, max = 13)
    private String isbn;

    // 상품 설명
    @NotBlank
    @Length(min = 1, max = 255)
    private String title;
    @NotBlank
    @Length(min = 1, max = 21844)
    private String contents;
    @NotBlank
    @Length(min = 1, max = 21844)
    private String description;

    // 저자, 출판사
    private List<Long> authors;
    @Positive
    private long publisherId;

    // 정가, 할인율, 포인트 적립율
    @PositiveOrZero
    @Max(2000000)
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
    @Length(max = 8)
    private String issn;
    private Boolean isSubscriptionAvailable;

    // 판매 여부
    private Boolean isSale;

    // 수량, 출간일, 노출우선순위
    @PositiveOrZero
    @Max(2000000)
    private long quantity;
    @NotBlank
    private String publishedDate;

    @Min(-100)
    @Max(100)
    private int preferentialShowRanking;

    // 썸네일 파일
    @NotBlank
    private String thumbnailFileUrl;
    @NotBlank
    private String thumbnailFileUploadDateTime;

    // e-book 파일
    private String ebookFileUrl;
    private String ebookFileUploadDateTime;

    // codes
    @NotBlank
    private String productTypeCode;
    @NotBlank
    private String productSavingMethodCode;

    // 태그
    private List<Long> tags;

    // 카테고리
    private List<Long> categories;


    /**
     * Dto를 바탕으로 Product entity 객체를 만들어 반환합니다.
     *
     * @param subscribeProduct  구독상품 엔터티
     * @param thumbnailFile     썸네일 파일 엔터티
     * @param ebookFile         e-book 파일 엔터티
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return Publish entity 객체
     * @author 이수정
     * @since 1.0
     */
    public Product toProductEntity(
            SubscribeProduct subscribeProduct,
            File thumbnailFile,
            File ebookFile,
            TotalDiscountRate totalDiscountRate
    ) {
        return Product.builder()
                .isbn(isbn)
                .title(title)
                .contents(contents)
                .description(description)
                .actualPrice(actualPrice)
                .discountRate(discountRate)
                .isSeparatelyDiscount(isSeparatelyDiscount)
                .givenPointRate(givenPointRate)
                .isGivenPoint(isGivenPoint)
                .isSubscriptionAvailable(isSubscriptionAvailable)
                .isSale(isSale)
                .isForcedOutOfStock(false)
                .quantity(quantity)
                .preferentialShowRanking(preferentialShowRanking)
                .subscribeProduct(subscribeProduct)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .totalDiscountRate(totalDiscountRate)
                .productTypeCode(ProductTypeCode.valueOf(productTypeCode))
                .productSavingMethodCode(ProductSavingMethodCode.valueOf(productSavingMethodCode))
                .build();
    }

    /**
     * Dto를 바탕으로 SubscribeProduct entity를 생성하여 반환합니다.
     *
     * @return SubscribeProduct entity 객체
     * @author 이수정
     * @since 1.0
     */
    public SubscribeProduct toSubscribeProductEntity() {
        return SubscribeProduct.builder().ISSN(issn).build();
    }

    /**
     * Dto를 바탕으로 ThumbnailFile entity를 생성하여 반환합니다.
     *
     * @return ThumbnailFile entity 객체
     * @author 이수정
     * @since 1.0
     */
    public File toThumbnailFileEntity() {
        return File.builder()
                .url(thumbnailFileUrl)
                .uploadDateTime(LocalDateTime.parse(
                        thumbnailFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }

    /**
     * Dto를 바탕으로 EbookFile entity를 생성하여 반환합니다.
     *
     * @return EbookFile entity 객체
     * @author 이수정
     * @since 1.0
     */
    public File toEbookFileEntity() {
        if (Objects.isNull(ebookFileUrl)) {
            return null;
        }

        return File.builder()
                .url(ebookFileUrl)
                .uploadDateTime(LocalDateTime.parse(
                        ebookFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }

    @Override
    public String toString() {
        return "product create";
    }
}
