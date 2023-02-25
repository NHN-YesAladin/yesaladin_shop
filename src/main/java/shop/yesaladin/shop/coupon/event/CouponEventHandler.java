package shop.yesaladin.shop.coupon.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@RequiredArgsConstructor
@Component
public class CouponEventHandler {

    private final GiveCouponService couponService;

    @EventListener(CouponGiveResponseEvent.class)
    public void consumeCouponGiveResponseMessage(CouponGiveResponseEvent event) {
        couponService.giveCouponToMember(event.getMessage());
    }
}
