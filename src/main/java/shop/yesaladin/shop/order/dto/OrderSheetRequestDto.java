package shop.yesaladin.shop.order.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 주문서에 필요한 데이터를 요청하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetRequestDto {

    @NotNull
    @NotEmpty
    private List<String> isbn;

    @NotNull
    @NotEmpty
    private List<Integer> quantity;

}
