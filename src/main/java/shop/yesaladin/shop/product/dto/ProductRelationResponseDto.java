package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRelationResponseDto {

    Long id;
    private String isbn;
    private String title;
    private long actualPrice;
    private int discountRate;
    private boolean isForcedOutOfStock;
    private long quantity;
    private int preferentialShowRanking;
    private List<String> authors;
    private String publisher;
    private LocalDate publishedDate;

    public static ProductRelationResponseDto createDto(
            Product product,
            List<String> authors,
            String publisher,
            LocalDate publishedDate
    ) {
        return ProductRelationResponseDto.builder()
                .id(product.getId())
                .isbn(product.getIsbn())
                .title(product.getTitle())
                .actualPrice(product.getActualPrice())
                .isForcedOutOfStock(product.isForcedOutOfStock())
                .quantity(product.getQuantity())
                .preferentialShowRanking(product.getPreferentialShowRanking())
                .authors(authors)
                .publisher(publisher)
                .publishedDate(publishedDate)
                .build();
    }
}
