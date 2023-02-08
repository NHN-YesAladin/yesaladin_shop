package shop.yesaladin.shop.coupon.adapter.kafka;

import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.MessageKey;

@EmbeddedKafka(topics = {"${coupon.topic.give-request}",
        "${coupon.topic.give-request-limit}"}, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093", "port=9093"})
@SpringBootTest
class CouponProducerTest {

    @Value("${coupon.topic.give-request}")
    private String giveRequestTopic;
    @Value("${coupon.topic.give-request-limit}")
    private String giveRequestLimitTopic;
    @Value("${coupon.topic.given}")
    private String givenTopic;

    @Autowired
    private CouponProducer couponProducer;
    @Autowired
    private EmbeddedKafkaBroker broker;
    @Autowired
    private ConsumerFactory<String, Object> consumerFactory;

    private Consumer<String, Object> consumer;

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    @DisplayName("수량 무제한 쿠폰 지급 요청 메시지가 발행된다.")
    void produceGiveRequestMessage() {
        // given
        consumer = consumerFactory.createConsumer("testGroup", "1");
        consumer.subscribe(List.of(giveRequestTopic));

        CouponGiveRequestMessage couponGiveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId("1")
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .couponId(1L)
                .build();

        // when
        couponProducer.produceGiveRequestMessage(couponGiveRequestMessage);

        // then

        ConsumerRecord<String, Object> actual = KafkaTestUtils.getSingleRecord(
                consumer,
                giveRequestTopic
        );
        CouponGiveRequestMessage actualValue = (CouponGiveRequestMessage) actual.value();
        Assertions.assertThat(actual.key()).isEqualTo(MessageKey.GIVE_REQUEST.name());
        Assertions.assertThat(actualValue.getCouponId()).isEqualTo(1L);
        Assertions.assertThat(actualValue.getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
        Assertions.assertThat(actualValue.getRequestId()).isEqualTo("1");
    }

    @Test
    @DisplayName("수량 제한 쿠폰 지급 요청 메시지가 발행된다.")
    void produceGiveRequestLimitMessage() {
        // given
        consumer = consumerFactory.createConsumer("testGroup", "1");
        consumer.subscribe(List.of(giveRequestLimitTopic));

        CouponGiveRequestMessage couponGiveRequestMessage = CouponGiveRequestMessage.builder()
                .requestId("1")
                .triggerTypeCode(TriggerTypeCode.SIGN_UP)
                .couponId(1L)
                .build();

        // when
        couponProducer.produceGiveRequestLimitMessage(couponGiveRequestMessage);

        // then

        ConsumerRecord<String, Object> actual = KafkaTestUtils.getSingleRecord(
                consumer,
                giveRequestLimitTopic
        );
        CouponGiveRequestMessage actualValue = (CouponGiveRequestMessage) actual.value();
        Assertions.assertThat(actual.key()).isEqualTo(MessageKey.GIVE_REQUEST.name());
        Assertions.assertThat(actualValue.getCouponId()).isEqualTo(1L);
        Assertions.assertThat(actualValue.getTriggerTypeCode()).isEqualTo(TriggerTypeCode.SIGN_UP);
        Assertions.assertThat(actualValue.getRequestId()).isEqualTo("1");
    }

    @Test
    @DisplayName("수량 제한 쿠폰 지급 요청 메시지가 발행된다.")
    void produceGivenResultMessage() {
        // given
        consumer = consumerFactory.createConsumer("testGroup", "1");
        consumer.subscribe(List.of(givenTopic));

        CouponCodesAndResultMessage resultMessage = CouponCodesAndResultMessage.builder()
                .success(true)
                .couponCodes(List.of("couponCode"))
                .build();

        // when
        couponProducer.produceGivenResultMessage(resultMessage);

        // then
        ConsumerRecord<String, Object> actual = KafkaTestUtils.getSingleRecord(
                consumer,
                givenTopic
        );
        CouponCodesAndResultMessage actualValue = (CouponCodesAndResultMessage) actual.value();
        Assertions.assertThat(actual.key()).isEqualTo(MessageKey.GIVEN.name());
        Assertions.assertThat(actualValue.isSuccess()).isTrue();
        Assertions.assertThat(actualValue.getCouponCodes()).containsOnly("couponCode");
    }
}