package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedCategories;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedFile;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTags;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchedProductResponseDto {
    private Long id;
    private String title;
    private int discountRate;
    private long sellingPrice;
    private Boolean isForcedOutOfStack;
    private SearchedFile thumbnailFileUrl;
    private SearchedPublisher publisher;
    private String publishedDate;
    private List<SearchedCategories> categories;
    private List<SearchedAuthor> authors;
    private List<SearchedTags> tags;
}
