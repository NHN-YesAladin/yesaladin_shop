package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.shop.product.domain.model.search.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.search.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.search.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.search.SearchedProductFile;
import shop.yesaladin.shop.product.domain.model.search.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.search.SearchedProductTag;


@Getter
@AllArgsConstructor
@Builder
public class SearchedProductResponseDto {
    private Long id;
    private String title;
    private int discountRate;
    private long sellingPrice;
    private Boolean isForcedOutOfStack;
    private SearchedProductFile thumbnailFileUrl;
    private SearchedProductPublisher publisher;
    private String publishedDate;
    private List<SearchedProductCategory> categories;
    private List<SearchedProductAuthor> authors;
    private List<SearchedProductTag> tags;

    public static SearchedProductResponseDto fromIndex(SearchedProduct searchedProduct) {
        return SearchedProductResponseDto.builder()
                .id(searchedProduct.getId())
                .title(searchedProduct.getTitle())
                .authors(searchedProduct.getAuthors())
                .sellingPrice(searchedProduct.getActualPrice() - searchedProduct.getActualPrice() *  (searchedProduct.getDiscountRate() / 100))
                .discountRate(searchedProduct.getDiscountRate())
                .publisher(searchedProduct.getPublisher())
                .categories(searchedProduct.getCategories())
                .publishedDate(searchedProduct.getPublishedDate().toString())
                .thumbnailFileUrl(searchedProduct.getThumbnailFile())
                .tags(searchedProduct.getTags())
                .isForcedOutOfStack(searchedProduct.getIsForcedOutOfStack())
                .build();
    }
}
