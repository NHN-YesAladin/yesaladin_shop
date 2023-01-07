package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.publisher.domain.model.Publisher;

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
    private String writerName;
    @NotBlank
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
    @NotBlank
    @Length(max = 9)
    private String ISSN;
    private Boolean isSubscriptionAvailable;

    // 판매 여부, 강제품절 여부
    private Boolean isSale;
    private Boolean isForcedOutOfStock;

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


    /**
     * DTO를 바탕으로 Product entity 객체를 만들어 리턴합니다.
     *
     * @param publisher 출판사 엔터티
     * @param subscribeProduct 구독상품 엔터티
     * @param thumbnailFile 썸네일 파일 엔터티
     * @param ebookFile e-book 파일 엔터티
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return Publish entity 객체
     * @author 이수정
     * @since 1.0
     */
    public Product toProductEntity(Publisher publisher, SubscribeProduct subscribeProduct, File thumbnailFile, File ebookFile, TotalDiscountRate totalDiscountRate) {
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
                .isForcedOutOfStock(isForcedOutOfStock)
                .quantity(quantity)
                .publishedDate(LocalDate.parse(publishedDate, DateTimeFormatter.ISO_DATE))
                .preferentialShowRanking(preferentialShowRanking)
                .publisher(publisher)
                .subscribeProduct(subscribeProduct)
                .thumbnailFile(thumbnailFile)
                .ebookFile(ebookFile)
                .totalDiscountRate(totalDiscountRate)
                .productTypeCode(ProductTypeCode.valueOf(productTypeCode))
                .productSavingMethodCode(ProductSavingMethodCode.valueOf(productSavingMethodCode))
                .build();
    }

    /**
     * DTO를 바탕으로 Publish entity 객체를 만들어 리턴합니다.
     *
     * @return Publish entity 객체
     * @author 이수정
     * @since 1.0
     */
    public Publisher toPublisherEntity() {
        return Publisher.builder().name(publisherName).build();
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
                .fileName(thumbnailFileName)
                .uploadDateTime(LocalDateTime.parse(thumbnailFileUploadDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
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
                .fileName(ebookFileName)
                .uploadDateTime(LocalDateTime.parse(ebookFileUploadDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    /**
     * DTO를 바탕으로 TotalDiscountRate entity 객체를 만들어 리턴합니다.
     *
     * @return TotalDiscountRate entity 객체
     * @author 이수정
     * @since 1.0
     */
    public TotalDiscountRate toTotalDiscountRateEntity() {
        return TotalDiscountRate.builder().discountRate(discountRate).build();
    }
}
