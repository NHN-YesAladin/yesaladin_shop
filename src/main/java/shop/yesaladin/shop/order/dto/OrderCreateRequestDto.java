package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

/**
 * 주문 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OrderCreateRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
    protected LocalDate expectedShippingDate;
    @NotEmpty
    @NotNull
    protected List<ProductOrderRequestDto> orderProducts;
    @Min(value = 0)
    protected long productTotalAmount;
    @Min(value = 0)
    protected int shippingFee;
    @Min(value = 0)
    protected int wrappingFee;
}
