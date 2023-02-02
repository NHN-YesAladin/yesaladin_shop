package shop.yesaladin.shop.order.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;

/**
 * 주문 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderNonMemberCreateRequestDto extends OrderCreateRequestDto {

    @NotBlank
    @Length(min = 1, max = 20)
    private String ordererName;
    @NotNull
    @Pattern(regexp = "^01([0|1])([0-9]{8})$")
    private String ordererPhoneNumber;

    @NotBlank
    @Length(min = 2, max = 255)
    private String ordererAddress;

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
    public NonMemberOrder toEntity(String name, String orderNumber, LocalDateTime orderDateTime) {
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
                .address(ordererAddress)
                .nonMemberName(ordererName)
                .phoneNumber(ordererPhoneNumber)
                .build();
    }
}
