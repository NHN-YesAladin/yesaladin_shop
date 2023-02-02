package shop.yesaladin.shop.message.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.message.Message;

/**
 * 메시지를 생산하는 생산자 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void produceMessage(String topic, Message message) {
        kafkaTemplate.send(topic, message);
    }

}
