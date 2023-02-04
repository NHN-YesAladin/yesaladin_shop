package shop.yesaladin.shop.coupon.adapter.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponConsumer {

    @KafkaListener(id = "give-request-response", topics = "${topic.give-request-response}")
    public void consumeGiveRequestResponseMessage() {

    }

    @KafkaListener(id = "use-request-response", topics = "${topic.use-request-response}")
    public void consumeUseRequestResponseMessage() {

    }


}
