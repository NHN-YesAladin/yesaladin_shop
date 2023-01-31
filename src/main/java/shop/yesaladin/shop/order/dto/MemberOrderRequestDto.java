package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.product.dto.OrderProductRequestDto;

/**
 * 회원 주문서에 필요한 데이터를 요청하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MemberOrderRequestDto {

    private List<OrderProductRequestDto> productList;
}
