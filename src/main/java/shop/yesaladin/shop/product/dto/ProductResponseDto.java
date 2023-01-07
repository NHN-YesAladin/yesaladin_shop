package shop.yesaladin.shop.product.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 상품 등록 후 등록한 상품의 id를 받아오는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
public class ProductResponseDto {
    private Long id;
}
