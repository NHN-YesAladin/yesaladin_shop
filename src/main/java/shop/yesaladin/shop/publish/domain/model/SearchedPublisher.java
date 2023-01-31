package shop.yesaladin.shop.publish.domain.model;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@AllArgsConstructor
@Document(indexName = "publisher", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedPublisher {
    @Id
    @Field(name = "id")
    Long id;

    @Field(name = "name")
    String name;
}
