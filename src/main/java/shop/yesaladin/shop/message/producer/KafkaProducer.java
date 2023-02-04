package shop.yesaladin.shop.message.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 메시지를 생산하는 생산자 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produceMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

}
