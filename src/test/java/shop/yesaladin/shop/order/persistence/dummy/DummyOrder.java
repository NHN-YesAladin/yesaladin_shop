package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

public class DummyOrder {

    private static final String orderNumber = "20230106-3942JE8";
    private static final LocalDateTime orderDateTime = LocalDateTime.of(2023, 1, 5, 0, 0);
    private static final LocalDate expectedTransportDate = LocalDate.of(2023, 3, 2);

    public static NonMemberOrder nonMemberOrder() {
        return NonMemberOrder.builder()
                .name("nonMemberOrder")
                .orderNumber(orderNumber + "n")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(10000L)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address("서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)")
                .nonMemberName("김몽머")
                .phoneNumber("01012341234")
                .build();
    }

    public static MemberOrder memberOrder(Member member, MemberAddress memberAddress) {
        return MemberOrder.builder()
                .orderNumber(orderNumber + "m")
                .name("memberOrder")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(1000L)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(9000L)
                .orderCode(OrderCode.MEMBER_ORDER)
                .memberAddress(memberAddress)
                .member(member)
                .build();
    }

    public static Subscribe subscribe(
            Member member,
            MemberAddress memberAddress,
            SubscribeProduct subscribeProduct
    ) {
        return Subscribe.builder()
                .orderNumber(orderNumber + "o")
                .name("subscribe")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(null)
                .isHidden(false)
                .usedPoint(1000L)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(9000L)
                .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                .memberAddress(memberAddress)
                .member(member)
                .expectedDay(5)
                .intervalMonth(6)
                .nextRenewalDate(orderDateTime.withDayOfMonth(5).toLocalDate().plusMonths(6))
                .subscribeProduct(subscribeProduct)
                .build();
    }
    public static NonMemberOrder nonMemberOrderWithId() {
        return NonMemberOrder.builder()
                .id(1L)
                .name("nonMemberOrder")
                .orderNumber(orderNumber + "n")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(10000L)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address("서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)")
                .nonMemberName("김몽머")
                .phoneNumber("01012341234")
                .build();
    }

    public static MemberOrder memberOrderWithId(Member member, MemberAddress memberAddress) {
        return MemberOrder.builder()
                .id(1L)
                .orderNumber(orderNumber + "m")
                .name("memberOrder")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(1000L)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(9000L)
                .orderCode(OrderCode.MEMBER_ORDER)
                .memberAddress(memberAddress)
                .member(member)
                .build();
    }

    public static Subscribe subscribeWithId(
            Member member,
            MemberAddress memberAddress,
            SubscribeProduct subscribeProduct
    ) {
        return Subscribe.builder()
                .id(1L)
                .orderNumber(orderNumber + "o")
                .name("subscribe")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(null)
                .isHidden(false)
                .usedPoint(1000L)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(9000L)
                .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                .memberAddress(memberAddress)
                .member(member)
                .expectedDay(5)
                .intervalMonth(6)
                .nextRenewalDate(orderDateTime.withDayOfMonth(5).toLocalDate().plusMonths(6))
                .subscribeProduct(subscribeProduct)
                .build();
    }
}
