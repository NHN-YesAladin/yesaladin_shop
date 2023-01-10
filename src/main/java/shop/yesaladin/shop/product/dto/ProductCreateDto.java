package shop.yesaladin.shop.product.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

/**
 * 상품 등록을 위한 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String writerName;
    private String loginId;
    @NotBlank
    private String publisherName;

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
    @Length(max = 9)
    private String ISSN;
    private Boolean isSubscriptionAvailable;

    // 판매 여부, 강제품절 여부
    private Boolean isSale;
//    private Boolean isForcedOutOfStock;

    // 수량, 출간일, 노출우선순위
    @PositiveOrZero
    private long quantity;
    @NotBlank
    private String publishedDate;
    @PositiveOrZero
    private int preferentialShowRanking;

    // 썸네일 파일
    @NotBlank
    private String thumbnailFileName;
    @NotBlank
    private String thumbnailFileUploadDateTime;

    // e-book 파일
    @NotBlank
    private String ebookFileName;
    @NotBlank
    private String ebookFileUploadDateTime;

    // codes
    @NotBlank
    private String productTypeCode;
    @NotBlank
    private String productSavingMethodCode;

    // 태그
    private List<String> tags;


    /**
     * DTO를 바탕으로 Product entity 객체를 만들어 리턴합니다.
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
                .ISBN(ISBN)
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
     * DTO를 바탕으로 SubscribeProduct entity 객체를 만들어 리턴합니다.
     *
     * @return SubscribeProduct entity 객체
     * @author 이수정
     * @since 1.0
     */
    public SubscribeProduct toSubscribeProductEntity() {
        return SubscribeProduct.builder().ISSN(ISSN).build();
    }

    /**
     * DTO를 바탕으로 ThumbnailFile entity 객체를 만들어 리턴합니다.
     *
     * @return ThumbnailFile entity 객체
     * @author 이수정
     * @since 1.0
     */
    public File toThumbnailFileEntity() {
        return File.builder()
                .name(thumbnailFileName)
                .uploadDateTime(LocalDateTime.parse(
                        thumbnailFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }

    /**
     * DTO를 바탕으로 EbookFile entity 객체를 만들어 리턴합니다.
     *
     * @return EbookFile entity 객체
     * @author 이수정
     * @since 1.0
     */
    public File toEbookFileEntity() {
        return File.builder()
                .name(ebookFileName)
                .uploadDateTime(LocalDateTime.parse(
                        ebookFileUploadDateTime,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ))
                .build();
    }
}
