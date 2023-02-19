package shop.yesaladin.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Spring Retry를 활성화합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@EnableRetry
@Configuration
public class RetryConfiguration {

}
