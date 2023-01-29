package shop.yesaladin.shop.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
public class SearchedProductCategory {
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