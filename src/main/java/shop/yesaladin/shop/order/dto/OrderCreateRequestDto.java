package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

/**
 * 주문 생성을 요청하는 dto 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCreateRequestDto {

    @NotBlank
    @Length(min = 1, max = 20)
    private String ordererName;
    @Pattern(regexp = "^01([0|1])([0-9]{8})$")
    private String ordererPhoneNumber;
    private Long ordererAddressId;
    @NotBlank
    @Length(min = 2, max = 255)
    private String ordererAddress;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
    private LocalDate expectedShippingDate;
    @NotEmpty
    private List<ProductOrderRequestDto> orderProducts;
    private List<Long> orderCoupons;
    @Min(value = 0)
    private Long orderPoint;
    @Min(value = 0)
    private long productTotalAmount;
    @Min(value = 0)
    private int shippingFee;
    @Min(value = 0)
    private int wrappingFee;
    @Min(value = 0)
    private long orderTotalAmount;
    @Range(min = 1, max = 31)
    private Integer expectedDay;
    @Pattern(regexp = "^(6|12)$")
    private Integer intervalMonth;

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
                .usedPoint(orderPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(productTotalAmount)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address(ordererAddress)
                .nonMemberName(ordererName)
                .phoneNumber(ordererPhoneNumber)
                .build();
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
                .usedPoint(orderPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(productTotalAmount)
                .orderCode(OrderCode.MEMBER_ORDER)
                .member(member)
                .memberAddress(address)
                .build();
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
                .orderCode(OrderCode.MEMBER_ORDER)
                .member(member)
                .memberAddress(address)
                .expectedDay(expectedDay)
                .intervalMonth(intervalMonth)
                .nextRenewalDate(nextRenewalDate)
                .subscribeProduct(subscribeProduct)
                .build();
    }
}
