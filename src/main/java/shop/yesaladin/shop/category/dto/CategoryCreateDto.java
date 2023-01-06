package shop.yesaladin.shop.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 생성을 위한 Dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public class CategoryCreateDto {

    @Length(min = 1)
    private String name;

    public Category toEntity() {
        return Category.builder().name(this.name).build();
    }
}
