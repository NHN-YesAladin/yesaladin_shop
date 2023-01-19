package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 생성 및 수정을 위해 사용하는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private Boolean isShown;
    private Integer order;
    private Long parentId;


    /**
     * 카테고리 수정을 위해 해당 dto 를 Category 로 변환
     *
     * @param id     카테고리 id
     * @param depth  2차 카테고리일 경우 '0', 1차 카테고리일 경우 '1'
     * @param parent 2차 카테고리일 경우 사용, 1차 카테고리일 경우 null 입력
     * @return
     */
    public Category toEntity(Long id, int depth, Category parent) {
        return Category.builder()
                .id(id)
                .name(this.name)
                .isShown(this.isShown)
                .order(this.order)
                .depth(depth)
                .parent(parent)
                .build();
    }

}
