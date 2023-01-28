package shop.yesaladin.shop.product.domain.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
public class SearchedProductAuthor {
    @Field(name = "id", type = FieldType.Long)
    private Long id;
    @Field(name = "name", type = FieldType.Keyword)
    private String name;
}