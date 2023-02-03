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

    /**
     * 주문서에 포인트 데이터를 저장합니다.
     *
     * @param point 회원의 소지한 포인트
     * @author 최예린
     * @since 1.0
     */
    public void setPoint(Long point) {
        this.point = point;
    }

    /**
     * 주문서에 주문상품 정보를 담습니다.
     *
     * @param orderProducts 주문상품 정보
     * @author 최예린
     * @since 1.0
     */
    public void setOrderProducts(List<ProductOrderResponseDto> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
