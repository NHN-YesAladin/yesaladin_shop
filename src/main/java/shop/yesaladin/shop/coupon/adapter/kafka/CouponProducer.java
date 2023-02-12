package shop.yesaladin.shop.coupon.adapter.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.shop.config.CouponProperties;

/**
 * 쿠폰 관련 메시지를 생산하는 생산자 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponProducer {

    private final CouponProperties couponProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produceGiveRequestMessage(CouponGiveRequestMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGiveRequestTopic(),
                message
        );
    }

    public void produceGiveRequestLimitMessage(CouponGiveRequestMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGiveRequestLimitTopic(),
                message
        );
    }

    public void produceGivenResultMessage(CouponCodesAndResultMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGivenTopic(),
                message
        );
    }

}
