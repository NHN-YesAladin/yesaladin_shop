package shop.yesaladin.shop.writing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@Document(indexName = "yesaladin_author", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedAuthor {
    @Id
    @Field
    Long id;

    @Field(name = "name")
    String name;

    @Field(name = "loginId")
    String loginId;
}
