package shop.yesaladin.shop.coupon.adapter.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponCodesMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponUseRequestMessage;
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

    /**
     * 쿠폰 지급 요청 메시지를 발행합니다.
     *
     * @param message 발행할 지급 요청 메시지
     */
    public void produceGiveRequestMessage(CouponGiveRequestMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGiveRequestTopic(),
                message
        );
    }

    /**
     * 수량 제한 쿠폰 지급 요청 메시지를 발행합니다.
     *
     * @param message 발행할 지급 요청 메시지
     */
    public void produceGiveRequestLimitMessage(CouponGiveRequestMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGiveRequestLimitTopic(),
                message
        );
    }

    /**
     * 지급 완료 메시지를 발행합니다.
     *
     * @param message 발행할 쿠폰 지급 결과 메시지
     */
    public void produceGivenResultMessage(CouponCodesAndResultMessage message) {
        kafkaTemplate.send(
                couponProperties.getCouponGivenTopic(),
                message
        );
    }

    /**
     * 사용 결과 메시지를 발행합니다.
     *
     * @param message 발행할 사용 결과 메시지
     */

    public void produceUsedResultMessage(CouponCodesAndResultMessage message) {
        kafkaTemplate.send(couponProperties.getCouponUsedTopic(), message);
    }

    /**
     * 사용 요청 메시지를 발행합니다.
     *
     * @param message 발행할 사용 요청 메시지
     */
    public void produceUseRequestMessage(CouponUseRequestMessage message) {
        kafkaTemplate.send(couponProperties.getCouponUseRequestTopic(), message);
    }

    /**
     * 사용 취소 메시지를 발행합니다.
     *
     * @param message 바행할 사용 취소 메시지
     */
    public void produceUseRequestCancelMessage(CouponCodesMessage message) {
        kafkaTemplate.send(couponProperties.getCouponUseRequestCancelTopic(), message);
    }
}
