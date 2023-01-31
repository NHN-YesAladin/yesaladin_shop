package shop.yesaladin.shop.category.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.Category.CategoryBuilder;

/**
 * 카테고리의 일부 데이터를 front server로 전달하기 위한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;

    private String name;

    private Boolean isShown;

    private Integer order;

    private Long parentId;

    private String parentName;

    /**
     * 입력받은 카테고리를 통해 CategoryResponse 변환 1차 카테고리일 경우(category.getParent() == null) : parentId,
     * parentName 없이 변환 2차 카테고리일 경우 : 모든 변수에 값을 채워서 전달
     *
     * @param category 변환하고자 하는 Category
     * @return CategoryResponse 카테고리의 일부 데이터
     */
    public static CategoryResponseDto fromEntity(Category category) {
        CategoryResponseDtoBuilder builder = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .isShown(category.isShown())
                .order(category.getOrder());

        if (Objects.isNull(category.getParent())) {
            return builder.build();
        }
        return builder.parentId(category.getParent().getId())
                .parentName(category.getParent().getName())
                .build();
    }

    public Category toEntity(Category parent) {
        CategoryBuilder categoryBuilder = Category.builder()
                .id(this.id)
                .name(this.name)
                .isShown(this.isShown)
                .order(this.order);
        if (Objects.isNull(parent)) {
            return categoryBuilder.build();
        }
        return categoryBuilder.parent(parent).build();
    }
}
