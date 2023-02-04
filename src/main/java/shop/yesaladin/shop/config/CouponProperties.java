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

    @Value("${topic.give-request}")
    public String couponGiveRequestTopic;

    @Value("${topic.given}")
    public String couponGivenTopic;

    @Value("${topic.give-request-limit}")
    public String couponGiveRequestLimitTopic;

    @Value("${topic.give-request-cancel}")
    public String couponGiveRequestCancelTopic;

    @Value("${topic.use-request}")
    public String couponUseRequestTopic;

    @Value("${topic.used}")
    public String couponUsedTopic;

    @Value("${topic.use-request-cancel}")
    public String couponUseRequestCancelTopic;

}
