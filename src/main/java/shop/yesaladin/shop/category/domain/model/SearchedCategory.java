package shop.yesaladin.shop.category.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;

/**
 * 상품 등록에서의 카테고리 검색 엘라스틱서치 인덱스
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "yesaladin_category", writeTypeHint = WriteTypeHint.FALSE)
public class SearchedCategory {
    @Id
    @Field(name = "id")
    Long id;
    @Field(name = "parent")
    Parent parent;
    @Field(name = "name")
    String name;
    @Field(name = "is_shown")
    Boolean isShown;

    public CategoryResponseDto toDto() {
        return CategoryResponseDto.builder()
                .id(id)
                .name(name)
                .parentId(parent.id)
                .parentName(parent.name)
                .isShown(isShown)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Parent {
        private Long id;
        private String name;
        private Boolean isShown;
    }
}
