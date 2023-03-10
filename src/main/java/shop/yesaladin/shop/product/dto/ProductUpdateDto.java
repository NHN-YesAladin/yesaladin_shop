package shop.yesaladin.shop.product.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
 * 상품 수정을 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

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
    private String thumbnailFileUrl;
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
     * Dto를 바탕으로 Product를 수정하여 반환합니다.
     *
     * @param product           기존에 존재하던 상품 엔터티
     * @param subscribeProduct  구독상품 엔터티
     * @param thumbnailFile     썸네일 파일 엔터티
     * @param ebookFile         e-book 파일 엔터티
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return Publish entity 객체
     * @author 이수정
     * @since 1.0
     */
    public Product changeProduct(
            Product product,
            SubscribeProduct subscribeProduct,
            File thumbnailFile,
            File ebookFile,
            TotalDiscountRate totalDiscountRate
    ) {
        return Product.builder()
                .id(product.getId())
                .isbn(product.getIsbn())
                .title(title)
                .contents(contents)
                .description(description)
                .actualPrice(actualPrice)
                .discountRate(discountRate)
                .isSeparatelyDiscount(isSeparatelyDiscount)
                .givenPointRate(givenPointRate)
                .isGivenPoint(isGivenPoint)
                .isSubscriptionAvailable(isSubscriptionAvailable)
                .isSale(product.isSale())
                .isForcedOutOfStock(false)
                .quantity(quantity)
                .preferentialShowRanking(preferentialShowRanking)
                .isDeleted(product.isDeleted())
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
     * @return 정보수정을 한 구독상품 엔터티
     * @author 이수정
     * @since 1.0
     */
    public SubscribeProduct toSubscribeProductEntity() {
        return SubscribeProduct.builder().ISSN(issn).build();
    }

    /**
     * DTO를 바탕으로 ThumbnailFile을 수정하여 반환합니다.
     *
     * @param thumbnailFile 기존의 썸네일 파일 엔터티
     * @return 정보수정을 한 썸네일 파일 엔터티
     * @author 이수정
     * @since 1.0
     */
    public File changeThumbnailFile(File thumbnailFile) {
        return File.builder()
                .id(thumbnailFile.getId())
                .url(thumbnailFileUrl)
                .uploadDateTime(LocalDateTime.parse(
                        thumbnailFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }

    /**
     * DTO를 바탕으로 EbookFile을 수정하여 반환합니다.
     *
     * @param ebookFile 기존의 E-book 파일 엔터티
     * @return 정보수정을 한 E-book 파일 엔터티
     * @author 이수정
     * @since 1.0
     */
    public File changeEbookFile(File ebookFile) {
        return File.builder()
                .id(ebookFile.getId())
                .url(ebookFileUrl)
                .uploadDateTime(LocalDateTime.parse(
                        ebookFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }
}
