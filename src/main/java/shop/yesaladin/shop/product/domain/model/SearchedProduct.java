package shop.yesaladin.shop.product.domain.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class SearchedProduct {
    private Long id;
    private String ISBN;
    private String title;
    private String contents;
    private String description;
    private Long actualPrice;
    private int discountRate;
    private boolean isSeparatelyDiscount;
    private boolean givenPointRate;
    private boolean isGivenPoint;
    private boolean isSubscriptionAvailable;
    private boolean isSale;
    private Long quantity;
    private boolean isForcedOutOfStack;
    private Long preferentialShowRanking;
    private String productType;
    private Long integratedDiscountRate;
    private String savingMethod;
    private String thumbnailFileName;
    private String ebookFileName;
    private String publisher;
    private LocalDate publishedDate;

}
