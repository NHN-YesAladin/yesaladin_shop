package shop.yesaladin.shop.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
public class SearchedProductSubscribProduct {
    @Field(name = "id", type = FieldType.Long)
    long id;
    @Field(name = "ISSN", type = FieldType.Keyword)
    String issn;
}