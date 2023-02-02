package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문한 상품의 정보를 보내기 위한 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class ProductOrderRequestDto {

    private String isbn;
    private int quantity;
}
