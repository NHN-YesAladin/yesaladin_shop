package shop.yesaladin.shop.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.shop.coupon.domain.model.CouponGiveRequestResponseMessage;

@Getter
public class CouponGiveResponseEvent extends ApplicationEvent {

    private final CouponGiveRequestResponseMessage message;

    public CouponGiveResponseEvent(Object source, CouponGiveRequestResponseMessage message) {
        super(source);
        this.message = message;
    }
}
