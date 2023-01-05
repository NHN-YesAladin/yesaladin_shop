package shop.yesaladin.shop.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.category.domain.model.Category;

@Getter
@AllArgsConstructor
public class CategoryCreateDto {

    @Length(min = 1)
    private String name;

    public Category toEntity() {
        return Category.builder().name(this.name).build();
    }
}
