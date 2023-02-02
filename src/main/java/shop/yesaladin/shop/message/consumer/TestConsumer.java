package shop.yesaladin.shop.message.consumer;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(id="test", topics = "test.sign-up")
    public void listenTestTopic(Map<String, Object> data) {
        log.info("data received {}", data);
    }

}
