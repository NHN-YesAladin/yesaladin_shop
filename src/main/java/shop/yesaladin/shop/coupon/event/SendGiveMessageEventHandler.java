package shop.yesaladin.shop.coupon.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendGiveMessageEventHandler implements ApplicationListener<SendGiveMessageEvent> {

    private final GiveCouponService couponService;
    private final GatewayProperties gatewayProperties;
    private final RestTemplate restTemplate;

    @Override
    @Retryable(backoff = @Backoff(delay = 1000L))
    public void onApplicationEvent(SendGiveMessageEvent event) {
        RequestEntity<CouponGiveRequestMessage> body = RequestEntity.post(
                        gatewayProperties.getCouponUrl() + "/give/request")
                .body(event.getCouponGiveRequestMessage());

        ResponseEntity<CouponGiveRequestResponseMessage> response = restTemplate.exchange(
                body,
                CouponGiveRequestResponseMessage.class
        );

        couponService.giveCouponToMember(response.getBody());

    }

    @Recover
    public void logError(Exception e, SendGiveMessageEvent event) {
        log.error("request id {} failed", event.getCouponGiveRequestMessage().getRequestId(), e);
    }
}
