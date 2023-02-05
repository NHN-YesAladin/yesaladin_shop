package shop.yesaladin.shop.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 쿠폰 관련 설정 값을 가져오기 위한 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Configuration
public class CouponProperties {

    @Value("${coupon.topic.give-request}")
    private String couponGiveRequestTopic;

    @Value("${coupon.topic.given}")
    private String couponGivenTopic;

    @Value("${coupon.topic.give-request-limit}")
    private String couponGiveRequestLimitTopic;

    @Value("${coupon.topic.give-request-cancel}")
    private String couponGiveRequestCancelTopic;

    @Value("${coupon.topic.use-request}")
    private String couponUseRequestTopic;

    @Value("${coupon.topic.used}")
    private String couponUsedTopic;

    @Value("${coupon.topic.use-request-cancel}")
    private String couponUseRequestCancelTopic;

}
