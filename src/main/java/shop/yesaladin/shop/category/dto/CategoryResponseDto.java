package shop.yesaladin.shop.category.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * front server 와 통신하기 위한 카테고리 Dto
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

    public static CategoryResponseDto fromEntity(Category category) {
        CategoryResponseDtoBuilder dtoBuilder = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .isShown(category.isShown())
                .order(category.getOrder());

        if (Objects.isNull(category.getParent())) {
            return dtoBuilder.build();
        }
        return dtoBuilder.parentId(category.getParent().getId())
                .parentName(category.getParent().getName())
                .build();
    }

}