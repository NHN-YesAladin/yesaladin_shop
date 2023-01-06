package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 수정을 위한 Dto
 * <p>
 * id는 명시적으로 작성하여도 되고 null도 가능하다.
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isShown;

    private Integer order;

    private Long parentId;

    public Category toEntity(Category parent) {
        return Category.builder()
                .id(this.id)
                .name(this.name)
                .isShown(this.isShown)
                .order(this.order)
                .parent(parent)
                .build();
    }

    public void setId(long id) {
        this.id = id;
    }
}
