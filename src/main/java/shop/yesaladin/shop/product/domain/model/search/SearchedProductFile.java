package shop.yesaladin.shop.product.domain.model.search;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
@Getter
@AllArgsConstructor
public class SearchedProductFile {
        @Field(name = "id", type = FieldType.Long)
        long id;
        @Field(name = "name", type = FieldType.Keyword)
        String name;
        @Field(name = "uploadDateTime", type = FieldType.Date)
        LocalDate uploadDateTime;
}