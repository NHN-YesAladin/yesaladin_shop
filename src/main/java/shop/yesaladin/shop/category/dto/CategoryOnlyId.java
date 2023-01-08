package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 카테고리 삭제를 위한 Dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public class CategoryOnlyId {

    @NotNull
    private long id;
}
