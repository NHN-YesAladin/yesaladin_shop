package shop.yesaladin.shop.product.dto;

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
    private List<String> authors;
    private List<String> tags;

    public static SearchedProductResponseDto fromIndex(
            SearchedProduct searchedProduct,
            Long sellingPrice,
            int rate
    ) {
        List<String> authors = new ArrayList<>();
        for(SearchedProductAuthor searchedProductAuthor: searchedProduct.getAuthors()) {
            authors.add(searchedProductAuthor.getName());
        }
        List<String> tags = new ArrayList<>();
        for(SearchedProductTag searchedProductTag: searchedProduct.getTags()) {
            tags.add(searchedProductTag.getName());
        }
        return SearchedProductResponseDto.builder()
                .id(searchedProduct.getId())
                .isbn(searchedProduct.getIsbn())
                .title(searchedProduct.getTitle())
                .quantity(searchedProduct.getQuantity())
                .sellingPrice(sellingPrice)
                .rate(rate)
                .isForcedOutOfStock(searchedProduct.getIsForcedOutOfStock())
                .thumbnailFile(searchedProduct.getThumbnailFile())
                .authors(authors)
                .tags(tags)
                .build();
    }
}
