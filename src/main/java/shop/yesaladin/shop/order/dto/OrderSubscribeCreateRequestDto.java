package shop.yesaladin.shop.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

/**
 * 정기구독 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
public class OrderSubscribeCreateRequestDto extends OrderMemberCreateRequestDto {

    @Range(min = 1, max = 31)
    private final int expectedDay;
    private final int intervalMonth;

    public OrderSubscribeCreateRequestDto(
            LocalDate expectedShippingDate,
            @Size(min = 1, max = 1, message = "구독 상품은 단건 주문만 가능합니다.") List<ProductOrderRequestDto> orderProducts,
            long productTotalAmount,
            int shippingFee,
            int wrappingFee,
            Long ordererAddressId,
            List<Long> orderCoupons,
            long orderPoint,
            Integer expectedDay,
            Integer intervalMonth
    ) {
        super(
                expectedShippingDate,
                orderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                ordererAddressId,
                orderCoupons,
                orderPoint
        );
        this.expectedDay = expectedDay;
        this.intervalMonth = intervalMonth;
    }

    /**
     * 요청 데이터를 회원 구독 주문 엔티티로 반환합니다.
     *
     * @param name             주문 이름
     * @param orderNumber      주문 번호
     * @param orderDateTime    주문 일시
     * @param member           회원
     * @param address          회원 배송지
     * @param nextRenewalDate  구독 갱신일
     * @param subscribeProduct 구독 상품
     * @return 생성한 회원 구독 주문
     * @author 최예린
     * @since 1.0
     */
    public Subscribe toEntity(
            String name,
            String orderNumber,
            LocalDateTime orderDateTime,
            Member member,
            MemberAddress address,
            LocalDate nextRenewalDate,
            SubscribeProduct subscribeProduct
    ) {
        return Subscribe.builder()
                .name(name)
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(null)
                .isHidden(false)
                .usedPoint(orderPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(productTotalAmount)
                .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                .member(member)
                .memberAddress(address)
                .expectedDay(expectedDay)
                .intervalMonth(intervalMonth)
                .nextRenewalDate(nextRenewalDate)
                .subscribeProduct(subscribeProduct)
                .build();
    }
}
