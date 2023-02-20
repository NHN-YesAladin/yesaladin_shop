package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 주문 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
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
    @NotBlank
    @Length(min = 1, max = 20)
    protected String recipientName;
    @NotBlank
    @Pattern(regexp = "^01([0|1])(\\d{8})$")
    protected String recipientPhoneNumber;
}
