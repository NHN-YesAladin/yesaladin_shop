package shop.yesaladin.shop.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

/**
 * 주문 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class OrderNonMemberCreateRequestDto extends OrderCreateRequestDto {

    @NotBlank
    @Length(min = 1, max = 20)
    private String ordererName;
    @NotNull
    @Pattern(regexp = "^01([0|1])(\\d{8})$")
    private String ordererPhoneNumber;

    @NotBlank
    @Length(min = 2, max = 255)
    private String ordererAddress;

    public OrderNonMemberCreateRequestDto(
            LocalDate expectedShippingDate,
            @NotEmpty @NotNull List<ProductOrderRequestDto> orderProducts,
            @Min(value = 0) long productTotalAmount,
            @Min(value = 0) int shippingFee,
            @Min(value = 0) int wrappingFee,
            @NotBlank String recipientName,
            @NotBlank String recipientPhoneNumber,
            String ordererName,
            String ordererPhoneNumber,
            String ordererAddress
    ) {
        super(
                expectedShippingDate,
                orderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber
        );
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.ordererAddress = ordererAddress;
    }

    /**
     * 요청 데이터를 비회원 주문 엔티티로 반환합니다.
     *
     * @param name          주문 이름
     * @param orderNumber   주문 번호
     * @param orderDateTime 주문 일시
     * @return 생성된 비회원 주문
     * @author 최예린
     * @since 1.0
     */
    public NonMemberOrder toEntity(
            String name,
            String orderNumber,
            LocalDateTime orderDateTime
    ) {
        return NonMemberOrder.builder()
                .name(name)
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedShippingDate)
                .isHidden(false)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(productTotalAmount)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .recipientName(recipientName)
                .recipientPhoneNumber(recipientPhoneNumber)
                .address(ordererAddress)
                .nonMemberName(ordererName)
                .phoneNumber(ordererPhoneNumber)
                .build();
    }
}
