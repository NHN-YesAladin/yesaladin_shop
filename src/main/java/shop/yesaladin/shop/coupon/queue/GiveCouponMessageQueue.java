package shop.yesaladin.shop.coupon.queue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

@Component
public class GiveCouponMessageQueue {

    private static final List<CouponGiveRequestResponseMessage> giveResponseMessageQueue = Collections.synchronizedList(
            new LinkedList<>());

    public void enqueueGiveResponseMessage(CouponGiveRequestResponseMessage message) {
        giveResponseMessageQueue.add(message);
    }

    public CouponGiveRequestResponseMessage dequeueGiveResponseMessage() {
        if (giveResponseMessageQueue.isEmpty()) {
            return null;
        }
        return giveResponseMessageQueue.remove(0);
    }
}
