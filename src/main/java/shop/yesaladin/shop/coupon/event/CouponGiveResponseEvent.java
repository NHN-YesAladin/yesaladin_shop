package shop.yesaladin.shop.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@Getter
public class CouponGiveResponseEvent extends ApplicationEvent {

    private final CouponGiveRequestResponseMessage message;

    public CouponGiveResponseEvent(Object source, CouponGiveRequestResponseMessage message) {
        super(source);
        this.message = message;
    }
}
