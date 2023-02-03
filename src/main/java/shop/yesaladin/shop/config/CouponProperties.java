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

    @Value("${coupon.topic.user-download-request}")
    private String userDownloadCouponRequestTopic;

    @Value("${coupon.topic.auto-issuance-request}")
    private String autoIssuanceCouponRequestTopic;
}
