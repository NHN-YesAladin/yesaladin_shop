package shop.yesaladin.shop.message.consumer;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponConsumer {

    @KafkaListener(id = "user-download-consumer-request", topics = "${coupon.topic.user-download-response}")
    public void consumeUserDownloadCouponTopic(Map<String, Object> data) {

    }

    @KafkaListener(id = "auto-issuance-consumer-response", topics = "${coupon.topic.auto-issuance-response}")
    public void consumeAutoIssuanceCouponTopic() {

    }

}
