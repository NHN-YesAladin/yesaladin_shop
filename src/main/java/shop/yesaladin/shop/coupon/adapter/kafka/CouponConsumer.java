package shop.yesaladin.shop.coupon.adapter.kafka;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

/**
 * 쿠폰 서버에서 들어오는 메시지를 소비하는 Consumer 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
@Component
public class CouponConsumer {

    private final GiveCouponService giveCouponService;
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 쿠폰 지급 응답 메시지를 구독합니다.
     *
     * @since 1.0
     */
    @KafkaListener(id = "${coupon.consumer-group.give-request-response}", topics = "${coupon.topic.give-request-response}")
    public void consumeGiveRequestResponseMessage(CouponGiveRequestResponseMessage message) {
        giveCouponService.giveCouponToMember(message);

        // 테스트를 위해 CountDownLatch 를 사용합니다.
        markAsConsumed();
    }

    /**
     * 쿠폰 사용 응답 메시지를 구독합니다.
     *
     * @since 1.0
     */
    @KafkaListener(id = "${coupon.consumer-group.give-request-response}", topics = "${coupon.topic.use-request-response}")
    public void consumeUseRequestResponseMessage() {

        markAsConsumed();
    }

    private void markAsConsumed() {
        latch.countDown();
        resetLatch();
    }

    private void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
