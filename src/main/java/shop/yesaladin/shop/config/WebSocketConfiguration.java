package shop.yesaladin.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 웹소켓을 사용하기 위한 Configuration 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns(
                "http://localhost:8080",
                "http://localhost:9090",
                "http://test.yesaladin.shop:9090",
                "https://www.yesaladin.shop"
        );
        registry.addEndpoint("/ws").setAllowedOriginPatterns(
                "http://localhost:8080",
                "http://localhost:9090",
                "http://test.yesaladin.shop:9090",
                "https://www.yesaladin.shop"
        ).withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/shop");
        registry.enableSimpleBroker("/ws/topic");
    }
}