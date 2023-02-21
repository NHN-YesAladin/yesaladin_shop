package shop.yesaladin.shop.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;

/**
 * 주문 기본 정보에 관한 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
public class OrderResponseDto {

    private String ordererName;
    private String ordererPhoneNumber;
    private LocalDateTime orderDateTime;
    private String recipientName;
    private String recipientPhoneNumber;
    private String orderAddress;
    private LocalDate expectedTransportDate;
    private Boolean isHidden;
    private OrderCode orderCode;
    private String orderNumber;
    private String orderName;
    private long usedPoint;
    private int shippingFee;
    private int wrappingFee;
    private long totalAmount;

    //주문 상태
    private OrderStatusCode orderStatusCode;

    //구독 정보
    private int expectedDay;
    private int intervalMonth;
    private LocalDate nextRenewalDate;

    public void setOrderInfoFromNonMemberOrder(NonMemberOrder nonMemberOrder) {
        setCommonOrderInfo(nonMemberOrder);
        this.ordererName = nonMemberOrder.getNonMemberName();
        this.ordererPhoneNumber = nonMemberOrder.getPhoneNumber();
        this.orderAddress = nonMemberOrder.getAddress();
    }

    public void setOrderInfoFromMemberOrder(MemberOrder memberOrder) {
        setCommonOrderInfo(memberOrder);
        this.ordererName = memberOrder.getMember().getName();
        this.ordererPhoneNumber = memberOrder.getMember().getPhone();
        this.orderAddress = memberOrder.getMemberAddress().getAddress();
    }

    public void setOrderInfoFromSubscribe(Subscribe subscribe) {
        setOrderInfoFromMemberOrder(subscribe);
        this.expectedDay = subscribe.getExpectedDay();
        this.intervalMonth = subscribe.getIntervalMonth();
        this.nextRenewalDate = subscribe.getNextRenewalDate();
    }

    private void setCommonOrderInfo(Order order) {
        this.orderCode = order.getOrderCode();
        this.orderName = order.getName();
        this.recipientName = order.getRecipientName();
        this.recipientPhoneNumber = order.getRecipientPhoneNumber();
        this.orderNumber = order.getOrderNumber();
        this.totalAmount = order.getTotalAmount();
        this.shippingFee = order.getShippingFee();
        this.wrappingFee = order.getWrappingFee();
        this.usedPoint = order.getUsedPoint();
        this.orderDateTime = order.getOrderDateTime();
        this.expectedTransportDate = order.getExpectedTransportDate();
        this.isHidden = order.isHidden();
    }

    public void setOrderStatusCode(OrderStatusCode orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }
}
