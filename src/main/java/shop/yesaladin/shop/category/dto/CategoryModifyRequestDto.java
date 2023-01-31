package shop.yesaladin.shop.category.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.Category.CategoryBuilder;

/**
 * 카테고리의 정보 변경을 위해 사용하는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModifyRequestDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Boolean isShown;
    @NotNull
    private Integer order;
    private Long parentId;
    private String parentName;

    /**
     * 카테고리 정보 변경을 위해 해당 dto 를 Category 로 변환
     *
     * @param parent 2차 카테고리일 경우 사용, 1차 카테고리일 경우 null 입력
     * @return
     */
    public Category toEntity(Category parent) {
        CategoryBuilder builder = Category.builder()
                .id(this.id)
                .name(this.name)
                .isShown(this.isShown)
                .order(this.order);
        if (Objects.isNull(parent)) {
            return builder.depth(Category.DEPTH_PARENT).build();
        }

        return builder.depth(Category.DEPTH_CHILD).parent(parent).build();
    }
}
