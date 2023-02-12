package shop.yesaladin.shop.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
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
public class OrderMemberCreateRequestDto extends OrderCreateRequestDto {

    @NotNull
    protected Long ordererAddressId;
    protected List<String> orderCoupons;
    @Min(value = 0)
    protected long usePoint;

    @Min(value = 0)
    protected long savePoint;

    public OrderMemberCreateRequestDto(
            LocalDate expectedShippingDate,
            @NotEmpty @NotNull List<ProductOrderRequestDto> orderProducts,
            @Min(value = 0) long productTotalAmount,
            @Min(value = 0) int shippingFee,
            @Min(value = 0) int wrappingFee,
            @NotBlank String recipientName,
            @NotBlank String recipientPhoneNumber,
            Long ordererAddressId,
            List<String> orderCoupons,
            long usePoint,
            long savePoint
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
        this.ordererAddressId = ordererAddressId;
        this.orderCoupons = orderCoupons;
        this.usePoint = usePoint;
        this.savePoint = savePoint;
    }

    /**
     * 요청 데이터를 회원 주문 엔티티로 반환합니다.
     *
     * @param name          주문 이름
     * @param orderNumber   주문 번호
     * @param orderDateTime 주문 일시
     * @param member        회원
     * @param address       회원 배송지
     * @return 생성된 회원 주문
     * @author 최예린
     * @since 1.0
     */
    public MemberOrder toEntity(
            String name,
            String orderNumber,
            LocalDateTime orderDateTime,
            Member member,
            MemberAddress address
    ) {
        return MemberOrder.builder()
                .name(name)
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedShippingDate)
                .isHidden(false)
                .usedPoint(usePoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(productTotalAmount)
                .orderCode(OrderCode.MEMBER_ORDER)
                .recipientName(recipientName)
                .recipientPhoneNumber(recipientPhoneNumber)
                .member(member)
                .memberAddress(address)
                .build();
    }
}
