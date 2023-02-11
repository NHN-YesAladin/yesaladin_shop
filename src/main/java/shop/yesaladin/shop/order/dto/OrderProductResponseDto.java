package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;

/**
 * 주문 상품을 조회 하기 위한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public class OrderProductResponseDto {

    private ProductOrderSheetResponseDto productDto;
    private int quantity;

}
