package shop.yesaladin.shop.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 쿠폰 서버의 정보를 담는 Configuration 클래스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@Configuration
public class CouponServerMetaConfig {

    @Value("${yesaladin.coupon.url}")
    private String couponServerUrl;
}
