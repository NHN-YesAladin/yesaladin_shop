package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
@Builder
public class SearchedProductDto {
    private Long id;
    private String title;
    private Long quantity;
    private int discountRate;
    private long sellingPrice;
    private Boolean isForcedOutOfStack;
    private String thumbnailFileUrl;
    private String publisher;
    private String publishedDate;
    private List<SearchedProductCategory> categories;
    private List<String> authors;
    private List<String> tags;

    public static SearchedProductDto fromIndex(SearchedProduct searchedProduct) {
        return SearchedProductDto.builder()
                .id(searchedProduct.getId())
                .title(searchedProduct.getTitle())
                .quantity(searchedProduct.getQuantity())
                .authors(searchedProduct.getAuthors().stream().map(SearchedProductAuthor::getName).collect(
                        Collectors.toList()))
                .sellingPrice(searchedProduct.getActualPrice() - searchedProduct.getActualPrice() * (searchedProduct.getDiscountRate() / 100))
                .discountRate(searchedProduct.getDiscountRate())
                .publisher(searchedProduct.getPublisher().getName())
                .categories(searchedProduct.getCategories())
                .publishedDate(searchedProduct.getPublishedDate().toString())
                .thumbnailFileUrl(searchedProduct.getThumbnailFile().getName())
                .tags(searchedProduct.getTags().stream().map(SearchedProductTag::getName).collect(
                        Collectors.toList()))
                .isForcedOutOfStack(searchedProduct.getIsForcedOutOfStack())
                .build();
    }
}
