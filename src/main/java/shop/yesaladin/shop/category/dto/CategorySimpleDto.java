package shop.yesaladin.shop.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
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
