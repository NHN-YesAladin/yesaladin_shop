package shop.yesaladin.shop.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 정기구독 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSubscribeCreateRequestDto extends OrderMemberCreateRequestDto {

    @NotNull
    @Range(min = 1, max = 31)
    private Integer expectedDay;
    @NotNull
    @Pattern(regexp = "^(6|12)$")
    private Integer intervalMonth;

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
