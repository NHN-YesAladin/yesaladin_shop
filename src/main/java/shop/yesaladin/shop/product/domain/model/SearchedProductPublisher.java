package shop.yesaladin.shop.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
public class SearchedProductPublisher {

        @Field(name = "id", type = FieldType.Integer)
        private Long id;
        @Field(name = "name", type = FieldType.Keyword)
        private String name;
}