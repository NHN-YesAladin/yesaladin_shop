package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 수정을 위한 Dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public class CategoryUpdateDto {

    @NotNull
    private long id;

    @Length(min = 1)
    private String name;

    @NotNull
    private boolean isShown;

    private Integer order;

    private Long parentId;

    public Category toEntity(@NotNull Category parent) {
        return Category.builder().id(this.id).name(this.name).isShown(this.isShown).order(order).parent(parent).build();
    }
}
