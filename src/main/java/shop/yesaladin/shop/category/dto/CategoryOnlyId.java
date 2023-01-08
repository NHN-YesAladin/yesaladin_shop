package shop.yesaladin.shop.category.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 카테고리 id만 전달하기위해 사용하는 dto
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
