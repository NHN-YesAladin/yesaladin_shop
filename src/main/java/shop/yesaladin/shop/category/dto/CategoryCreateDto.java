package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 생성을 위한 Dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {
    @NotBlank
    private String name;

    public Category toEntity() {
        return Category.builder().name(this.name).build();
    }
}
