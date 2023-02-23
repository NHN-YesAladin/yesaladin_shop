package shop.yesaladin.shop.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;

@Getter
public class SendGiveMessageEvent extends ApplicationEvent {

    private final CouponGiveRequestMessage couponGiveRequestMessage;

    public SendGiveMessageEvent(Object source, CouponGiveRequestMessage couponGiveRequestMessage) {
        super(source);
        this.couponGiveRequestMessage = couponGiveRequestMessage;
    }
}
