package shop.yesaladin.shop.publish.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@Document(indexName = "yesaladin_publisher", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedPublisher {
    @Id
    @Field(name = "id")
    Long id;

    @Field(name = "name")
    String name;
}
