package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 주문한 상품의 정보를 보내기 위한 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderRequestDto {

    @NotBlank
    private String isbn;
    @Min(1)
    private int quantity;
}
