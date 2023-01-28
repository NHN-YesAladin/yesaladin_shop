package shop.yesaladin.shop.product.domain.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
public class SearchedProductTotalDiscountRate {

        @Field(name = "id", type = FieldType.Integer)
        private int id;
        @Field(name = "discount_rate", type = FieldType.Integer)
        private int discountRate;
}