package shop.yesaladin.shop.category.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "category", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedCategory {

}
