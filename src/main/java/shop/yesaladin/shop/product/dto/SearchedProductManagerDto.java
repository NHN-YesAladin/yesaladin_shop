package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductFile;
import shop.yesaladin.shop.product.domain.model.SearchedProductProductType;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductSubscribProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;

@Getter
@Builder
@AllArgsConstructor
public class SearchedProductManagerDto {

    private Long id;
    private String isbn;
    private String title;
    private long actualPrice;
    private int discountRate;
    private boolean isSeparatelyDiscount;
    private int givenPointRate;
    private Boolean isGivenPoint;
    private Boolean isSubscriptionAvailable;
    private Boolean isSale;
    private Long quantity;
    private long preferentialShowRanking;
    private SearchedProductProductType productType;
    private SearchedProductTotalDiscountRate searchedTotalDiscountRate;
    private SearchedProductFile thumbnailFile;
    private SearchedProductFile ebookFile;
    private SearchedProductPublisher publisher;
    private LocalDate publishedDate;
    private String savingMethod;
    private List<SearchedProductCategory> categories;
    private List<SearchedProductAuthor> authors;
    private List<SearchedProductTag> tags;
    private List<SearchedProductSubscribProduct> subscribeProducts;

    public static SearchedProductManagerDto fromIndex(SearchedProduct searchedProduct) {
        return SearchedProductManagerDto.builder()
                .id(searchedProduct.getId())
                .isbn(searchedProduct.getIsbn())
                .title(searchedProduct.getTitle())
                .actualPrice(searchedProduct.getActualPrice())
                .discountRate(searchedProduct.getDiscountRate())
                .isSeparatelyDiscount(searchedProduct.isSeparatelyDiscount())
                .givenPointRate(searchedProduct.getGivenPointRate())
                .isGivenPoint(searchedProduct.isGivenPoint())
                .isSale(searchedProduct.isSale())
                .quantity(searchedProduct.getQuantity())
                .preferentialShowRanking(searchedProduct.getPreferentialShowRanking())
                .productType(searchedProduct.getProductType())
                .searchedTotalDiscountRate(searchedProduct.getSearchedTotalDiscountRate())
                .thumbnailFile(searchedProduct.getThumbnailFile())
                .ebookFile(searchedProduct.getEbookFile())
                .publishedDate(searchedProduct.getPublishedDate())
                .savingMethod(searchedProduct.getSavingMethod())
                .categories(searchedProduct.getCategories())
                .authors(searchedProduct.getAuthors())
                .tags(searchedProduct.getTags())
                .subscribeProducts(searchedProduct.getSubscribeProduct())
                .build();
    }
}
