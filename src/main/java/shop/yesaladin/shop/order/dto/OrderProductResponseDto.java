package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductOrderQueryResponseDto;

/**
 * 주문 상품을 조회 하기 위한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public class OrderProductResponseDto {

    private ProductOrderQueryResponseDto productDto;
    private int quantity;

    public static OrderProductResponseDto fromEntity(OrderProduct orderProduct, int quantity) {
        Product pd = orderProduct.getProduct();
        return new OrderProductResponseDto(new ProductOrderQueryResponseDto(
                pd.getId(),
                pd.getIsbn(),
                pd.getTitle(),
                pd.getActualPrice(),
                pd.getDiscountRate(),
                pd.isGivenPoint(),
                pd.getGivenPointRate(),
                pd.getQuantity()
        ), quantity);
    }

}
