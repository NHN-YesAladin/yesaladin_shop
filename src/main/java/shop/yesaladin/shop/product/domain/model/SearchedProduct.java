package shop.yesaladin.shop.product.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import java.time.LocalDate;
import java.util.List;

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
    private Boolean isForcedOutOfStack;
    @Field(name = "preferential_show_ranking", type = FieldType.Long)
    private long preferentialShowRanking;
    @Field(name = "product_type", type = FieldType.Object)
    private SearchedProductProductType productType;
    @Field(name = "integrated_discount_rate", type = FieldType.Object)
    private SearchedProductTotalDiscountRate searchedTotalDiscountRate;
    @Field(name = "thumbnail_file", type = FieldType.Object)
    private SearchedProductFile thumbnailFile;
    @Field(name = "ebook_file", type = FieldType.Object)
    private SearchedProductFile ebookFile;
    @Field(name = "publisher", type = FieldType.Object)
    private SearchedProductPublisher publisher;
    @Field(name = "published_date", type = FieldType.Date)
    private LocalDate publishedDate;
    @Field(name = "saving_method", type = FieldType.Keyword)
    private String savingMethod;
    @Field(name = "is_deleted", type = FieldType.Boolean)
    private Boolean isDeleted;
    @Field(name = "categories", type = FieldType.Object)
    private List<SearchedProductCategory> categories;
    @Field(name = "authors", type = FieldType.Object)
    private List<SearchedProductAuthor> authors;
    @Field(name = "tags", type = FieldType.Object)
    private List<SearchedProductTag> tags;
    @Field(name = "SubscribeProduct", type = FieldType.Object)
    private List<SearchedProductSubscribProduct> subscribeProduct;
}
