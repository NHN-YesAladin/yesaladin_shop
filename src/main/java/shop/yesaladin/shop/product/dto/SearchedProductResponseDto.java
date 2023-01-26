package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedCategories;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedFile;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedIssn;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedProductType;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTags;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTotalDiscountRate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchedProductResponseDto {
    private Long id;
    private String ISBN;
    private String title;
    private String contents;
    private String description;
    private long actualPrice;
    private int discountRate;
    private long sellingPrice;
    private long pointPrice;
    private boolean isSeparatelyDiscount;
    private int givenPointRate;
    private boolean isGivenPoint;
    private boolean isSubscriptionAvailable;
    private boolean isSale;
    private Long quantity;
    private boolean isForcedOutOfStack;
    private int preferentialShowRanking;
    private SearchedProductType productType;
    private SearchedTotalDiscountRate searchedTotalDiscountRate;
    private SearchedFile thumbnailFileUrl;
    private SearchedFile ebookFileUrl;
    private SearchedPublisher publisher;
    private String publishedDate;
    private List<SearchedCategories> categories;
    private List<SearchedAuthor> authors;
    private List<SearchedTags> tags;
    private List<Long> relatedProduct;
    private List<SearchedIssn> subscribeProduct;
}
