package shop.yesaladin.shop.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 웹소켓 토픽의 prefix를 가져옵니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Configuration
public class SocketProperties {

    @Value("${socket.topic-prefix.coupon-give-result}")
    private String couponGiveResultTopicPrefix;

    @Value("${socket.topic-prefix.coupon-use-result}")
    private String couponUseResultTopicPrefix;
}
