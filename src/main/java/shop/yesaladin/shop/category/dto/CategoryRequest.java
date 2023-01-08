package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CategoryRequest {

    @NotBlank
    private String name;

    @NotNull
    private Boolean isShown;

    private Long parentId;

    public Category toEntity(Category parent) {
        return Category.builder().name(this.name).isShown(this.isShown).parent(parent).build();
    }

    public Category toEntity(Long id, Category parent) {
        return Category.builder()
                .id(id)
                .name(this.name)
                .isShown(this.isShown)
                .parent(parent)
                .build();
    }

}
