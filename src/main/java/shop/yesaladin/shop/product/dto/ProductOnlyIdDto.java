package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상품의 아이디만 반환받는 경우 사용하는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOnlyIdDto {
    private Long id;
}
