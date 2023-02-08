package shop.yesaladin.shop.coupon.adapter.kafka;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@EmbeddedKafka(topics = {"${coupon.topic.give-request}",
        "${coupon.topic.give-request-limit}"}, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"})
@SpringBootTest
class CouponConsumerTest {

    @Value("${coupon.topic.give-request-response}")
    private String giveRequestResponseTopic;
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private CouponConsumer couponConsumer;
    @MockBean
    private GiveCouponService giveCouponService;
    @Autowired
    private EmbeddedKafkaBroker broker;

    @BeforeEach
    void setUp() {
        // consumer를 테스트하는데 있어 producer의 설정이 영향을 주지 않도록 최소한의 설정을 직접 정의합니다.
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DefaultKafkaProducerFactory<String, Object> pf = new DefaultKafkaProducerFactory<>(
                producerProps);
        kafkaTemplate = new KafkaTemplate<>(pf);
    }

    @Test
    @DisplayName("쿠폰 발행 요청 응답 메시지가 들어오면 쿠폰 지급을 시도한다.")
    void consumeCouponGiveRequestResponseMessageTest() throws InterruptedException {
        // when
        kafkaTemplate.send(
                giveRequestResponseTopic,
                new CouponGiveRequestResponseMessage("test", Collections.emptyList(), true, null)
        );

        // then
        boolean messageConsumed = couponConsumer.getLatch().await(10, TimeUnit.SECONDS);
        Assertions.assertThat(messageConsumed).isTrue();
        Mockito.verify(giveCouponService, Mockito.times(1))
                .giveCouponToMember(Mockito.argThat(arg -> arg.isSuccess() && arg.getRequestId()
                        .equals("test") && arg.getCoupons().isEmpty()
                        && Objects.isNull(arg.getErrorMessage())));
    }
}
