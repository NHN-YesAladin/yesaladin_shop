package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;

/**
 * 엘라스틱서치 상품 업데이트
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedProductUpdateDto {

    @NotBlank
    @Length(max = 255)
    private String title;
    @NotBlank
    private String contents;
    @NotBlank
    private String description;
    // 저자, 출판사
    private List<Long> authors;
    @Positive
    private long publisherId;
    @PositiveOrZero
    private long actualPrice;
    @PositiveOrZero
    @Max(100)
    private int discountRate;
    private Boolean isSeparatelyDiscount;
    private String isbn;
    private boolean isSale;
    private int preferentialShowRanking;
    private String thumbnailFileUrl;
    // 태그
    private List<Long> tags;
    // 카테고리
    private List<Long> categories;
    // 강제 품절
    private boolean isForcedOutOfStock;
    private LocalDate publishedDate;

    public static SearchedProduct toIndex(
            SearchedProductUpdateDto updateDto,
            Long id,
            SearchedProductTotalDiscountRate searchedProductTotalDiscountRate,
            List<SearchedProductTag> tags,
            List<SearchedProductCategory> categories,
            List<SearchedProductAuthor> authors,
            SearchedProductPublisher publisher
    ) {
        return SearchedProduct.builder()
                .id(id)
                .title(updateDto.getTitle())
                .contents(updateDto.getContents())
                .description(updateDto.getDescription())
                .isbn(updateDto.getIsbn())
                .actualPrice(updateDto.getActualPrice())
                .discountRate(updateDto.getDiscountRate())
                .isSeparatelyDiscount(updateDto.getIsSeparatelyDiscount())
                .isSale(updateDto.isSale())
                .preferentialShowRanking(updateDto.getPreferentialShowRanking())
                .thumbnailFile(updateDto.getThumbnailFileUrl())
                .isForcedOutOfStock(updateDto.isForcedOutOfStock())
                .publishedDate(updateDto.getPublishedDate())
                .searchedTotalDiscountRate(searchedProductTotalDiscountRate)
                .tags(tags)
                .categories(categories)
                .authors(authors)
                .publisher(publisher)
                .build();
    }
}
