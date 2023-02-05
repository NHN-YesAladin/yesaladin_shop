package shop.yesaladin.shop.coupon.adapter.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 쿠폰 서버에서 들어오는 메시지를 소비하는 Consumer 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponConsumer {

    /**
     * 쿠폰 지급 응답 메시지를 구독합니다.
     *
     * @since 1.0
     */
    @KafkaListener(id = "give-request-response", topics = "${coupon.topic.give-request-response}")
    public void consumeGiveRequestResponseMessage() {

    }

    /**
     * 쿠폰 사용 응답 메시지를 구독합니다.
     *
     * @since 1.0
     */
    @KafkaListener(id = "use-request-response", topics = "${coupon.topic.use-request-response}")
    public void consumeUseRequestResponseMessage() {

    }


}
