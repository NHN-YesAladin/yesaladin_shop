package shop.yesaladin.shop.product.domain.model;

import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

/**
 * 엘라스틱 서치 상품 인덱스
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedProduct {
    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;
    @Field(name = "ISBN", type = FieldType.Keyword)
    private String isbn;
    @Field(name = "title", type = FieldType.Text)
    private String title;
    @Field(name = "contents", type = FieldType.Text)
    private String contents;
    @Field(name = "description", type = FieldType.Text)
    private String description;
    @Field(name = "actual_price", type = FieldType.Long)
    private long actualPrice;
    @Field(name = "discount_rate", type = FieldType.Integer)
    private int discountRate;
    @Field(name = "is_separately_discount", type = FieldType.Boolean)
    private boolean isSeparatelyDiscount;
    @Field(name = "given_point_rate", type = FieldType.Integer)
    private int givenPointRate;
    @Field(name = "is_given_point", type = FieldType.Boolean)
    private boolean isGivenPoint;
    @Field(name = "is_subscription_available", type = FieldType.Boolean)
    private boolean isSubscriptionAvailable;
    @Field(name = "is_sale", type = FieldType.Boolean)
    private boolean isSale;
    @Field(name = "quantity", type = FieldType.Long)
    private Long quantity;
    @Field(name = "is_forced_out_of_stack", type = FieldType.Boolean)
    private boolean isForcedOutOfStack;
    @Field(name = "preferential_show_ranking", type = FieldType.Long)
    private long preferentialShowRanking;
    @Field(name = "product_type", type = FieldType.Object)
    private SearchedProductType productType;
    @Field(name = "integrated_discount_rate", type = FieldType.Object)
    private SearchedTotalDiscountRate searchedTotalDiscountRate;
    @Field(name = "thumbnail_file", type = FieldType.Object)
    private SearchedFile thumbnailFile;
    @Field(name = "ebook_file", type = FieldType.Object)
    private SearchedFile ebookFile;
    @Field(name = "publisher", type = FieldType.Object)
    private SearchedPublisher publisher;
    @Field(name = "published_date", type = FieldType.Date)
    private LocalDate publishedDate;
    @Field(name = "saving_method", type = FieldType.Keyword)
    private String savingMethod;
    @Field(name = "categories", type = FieldType.Object)
    private List<SearchedCategories> categories;
    @Field(name = "authors", type = FieldType.Object)
    private List<SearchedAuthor> authors;
    @Field(name = "tags", type = FieldType.Object)
    private List<SearchedTags> tags;
    @Field(name = "SubscribeProduct", type = FieldType.Object)
    private List<SubscribeProduct> subscribeProduct;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SearchedFile {
        @Field(name = "id", type = FieldType.Long)
        long id;
        @Field(name = "name", type = FieldType.Keyword)
        String name;
        @Field(name = "uploadDateTime", type = FieldType.Date)
        LocalDate uploadDateTime;
    }

    @Getter
    @AllArgsConstructor
    public static class SubscribeProduct {
        @Field(name = "id", type = FieldType.Long)
        long id;
        @Field(name = "ISSN", type = FieldType.Keyword)
        String issn;
    }
    @Getter
    @AllArgsConstructor
    public static class SearchedCategories {
        @Field(name = "id", type = FieldType.Long)
        private final Long id;
        @Field(name = "parent", type = FieldType.Long)
        private final Long parent;
        @Field(name = "name", type = FieldType.Keyword)
        private final String name;
        @Field(name = "is_shown", type = FieldType.Boolean)
        private final Boolean isShown;
        @Field(name = "disable", type = FieldType.Boolean)
        private final Boolean disable;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchedTags {
        @Field(name = "id", type = FieldType.Long)
        private Long id;
        @Field(name = "name", type = FieldType.Keyword)
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchedAuthor {
        @Field(name = "id", type = FieldType.Long)
        private Long id;
        @Field(name = "name", type = FieldType.Keyword)
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchedProductType {
        @Field(name = "id", type = FieldType.Integer)
        private int id;
        @Field(name = "name", type = FieldType.Keyword)
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchedTotalDiscountRate {
        @Field(name = "id", type = FieldType.Integer)
        private int id;
        @Field(name = "discount_rate", type = FieldType.Integer)
        private int discountRate;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchedPublisher {
        @Field(name = "id", type = FieldType.Integer)
        private Long id;
        @Field(name = "name", type = FieldType.Keyword)
        private String name;
    }

    public SearchedProductResponseDto toDto() {
        return SearchedProductResponseDto.builder()
                .id(id)
                .title(title)
                .authors(authors)
                .sellingPrice(actualPrice - actualPrice *  (discountRate / 100))
                .discountRate(discountRate)
                .publisher(publisher)
                .categories(categories)
                .publishedDate(publishedDate.toString())
                .thumbnailFileUrl(thumbnailFile)
                .authors(authors)
                .tags(tags)
                .build();
    }
}
