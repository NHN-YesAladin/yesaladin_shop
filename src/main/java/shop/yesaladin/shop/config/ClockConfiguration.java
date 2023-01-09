package shop.yesaladin.shop.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 타임머신 테스트를 위한 clock 객체를 빈으로 생성하는 Spring Configuration 클래스.
 *
 * @author 김홍대
 * @since 1.0
 */
@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
