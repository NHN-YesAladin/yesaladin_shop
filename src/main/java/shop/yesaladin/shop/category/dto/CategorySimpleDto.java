package shop.yesaladin.shop.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 카테고리에 대한 기본 정보 아이디, 이름, 노출여부, 순서 에 대한 정보를 담고있는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@NoArgsConstructor
public class CategorySimpleDto {
    private Long id;

    private String name;

    private Boolean isShown;

    private Integer order;

    @QueryProjection
    public CategorySimpleDto(Long id, String name, Boolean isShown, Integer order) {
        this.id = id;
        this.name = name;
        this.isShown = isShown;
        this.order = order;
    }
}
