package shop.yesaladin.shop.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.queue.GiveCouponMessageQueue;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupons/messages")
public class CouponMessageController {

    private final GiveCouponMessageQueue messageQueue;

    @PostMapping("/give/response")
    public void enqueueGiveResponseMessage(@RequestBody CouponGiveRequestResponseMessage message) {
        messageQueue.enqueueGiveResponseMessage(message);
    }
}
