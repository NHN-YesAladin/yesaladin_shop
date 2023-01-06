package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.domain.dummy.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrder;

public class DummyOrder {

    private static String orderNumber = "20230106-3942JE84";
    private static LocalDateTime orderDateTime = LocalDateTime.now();
    private static LocalDate expectedTransportDate = LocalDate.now();

    public static NonMemberOrder nonMemberOrder() {
        return NonMemberOrder.builder()
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(0)
                .wrappingFee(0)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address("GwangJu GwangYeokSi")
                .name("non-member")
                .phoneNumber("010-1234-1234")
                .build();
    }

    public static MemberOrder memberOrder(Member member, MemberAddress memberAddress) {
        return MemberOrder.builder()
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(0)
                .wrappingFee(0)
                .orderCode(OrderCode.MEMBER_ORDER)
                .memberAddress(memberAddress)
                .member(member)
                .build();
    }

    public static SubscribeOrder subscribeOrder(Subscribe subscribe) {
        return SubscribeOrder.builder()
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(0)
                .wrappingFee(0)
                .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                .isTransported(false)
                .expectedDate(LocalDate.now())
                .subscribe(subscribe)
                .build();
    }
}
