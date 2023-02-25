package shop.yesaladin.shop.coupon.scheduler;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.event.CouponGiveResponseEvent;
import shop.yesaladin.shop.coupon.queue.GiveCouponMessageQueue;

@RequiredArgsConstructor
@Component
public class CouponScheduler {

    private final GiveCouponMessageQueue messageQueue;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 500)
    public void consumeCouponGiveResponseMessage() {
        CouponGiveRequestResponseMessage message = messageQueue.dequeueGiveResponseMessage();
        if (Objects.nonNull(message)) {
            eventPublisher.publishEvent(new CouponGiveResponseEvent(this, message));
        }
    }
}
