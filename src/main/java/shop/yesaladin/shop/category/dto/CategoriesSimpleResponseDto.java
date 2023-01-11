package shop.yesaladin.shop.category.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 메인화면에서 필요한 데이터를 전달하기 위한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesSimpleResponseDto {

    CategorySimpleDto parent;
    private List<CategorySimpleDto> children;
}
