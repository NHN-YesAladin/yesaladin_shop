package shop.yesaladin.shop.tag.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

/**
 * 상품 등록에서 태그를 엘라스틱 서치에서 검색하는 인덱 모델
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "yesaladin_tag", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedTag {
    @Id
    @Field(name = "id")
    Long id;
    @Field(name = "name")
    String name;
}
