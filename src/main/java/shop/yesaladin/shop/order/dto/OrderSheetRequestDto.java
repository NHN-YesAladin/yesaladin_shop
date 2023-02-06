package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 주문서에 필요한 데이터를 요청하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetRequestDto {

    private List<String> isbnList;

    private List<Integer> quantityList;

}
