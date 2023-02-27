package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedProductResponseDto {

    private Long id;
    private String isbn;
    private String title;
    private Long quantity;
    private Long sellingPrice;
    private int rate;
    private Boolean isForcedOutOfStock;
    private String thumbnailFile;
    private String publisher;
    private Boolean isEbook;
    private Boolean isSubscriptionAvailable;
    private LocalDate publishedDate;
    private List<String> authors;
    private List<String> tags;

    public static SearchedProductResponseDto fromIndex(
            SearchedProduct searchedProduct,
            Long sellingPrice,
            int rate,
            Boolean isEbook
    ) {
        List<String> authors = new ArrayList<>();
        for (SearchedProductAuthor searchedProductAuthor : searchedProduct.getAuthors()) {
            authors.add(searchedProductAuthor.getName());
        }
        List<String> tags = new ArrayList<>();
        for (SearchedProductTag searchedProductTag : searchedProduct.getTags()) {
            tags.add(searchedProductTag.getName());
        }
        return SearchedProductResponseDto.builder()
                .id(searchedProduct.getId())
                .isbn(searchedProduct.getIsbn())
                .title(searchedProduct.getTitle())
                .quantity(searchedProduct.getQuantity())
                .sellingPrice(sellingPrice)
                .rate(rate)
                .publisher(searchedProduct.getPublisher().getName())
                .isForcedOutOfStock(searchedProduct.getIsForcedOutOfStock())
                .isSubscriptionAvailable(searchedProduct.getIsSubscriptionAvailable())
                .isEbook(isEbook)
                .publishedDate(searchedProduct.getPublishedDate())
                .thumbnailFile(searchedProduct.getThumbnailFile())
                .authors(authors)
                .tags(tags)
                .build();
    }
}
