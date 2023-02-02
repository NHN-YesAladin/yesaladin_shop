package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.Getter;
import shop.yesaladin.shop.product.dto.ProductOrderResponseDto;

/**
 * 회원 주문서에 필요한 데이터를 반환하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
public class OrderSheetResponseDto {

    private String name;
    private String phoneNumber;
    private Long point;
    private String address;
    private List<ProductOrderResponseDto> orderProducts;

    public OrderSheetResponseDto(
            String name,
            String phoneNumber,
            String address
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public OrderSheetResponseDto(List<ProductOrderResponseDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public void setOrderProducts(List<ProductOrderResponseDto> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
