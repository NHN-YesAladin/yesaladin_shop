package shop.yesaladin.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스
 *
 * @author : 송학현
 * @since : 1.0
 */
@Configuration
public class RestTemplateConfig {

    /**
     * client와 server간 요청, 응답을 위한 RestTemplate Bean 설정.
     *
     * @param clientHttpRequestFactory client, server 커넥션 설정 factory class.
     * @return RestTemplate 반환.
     * @author : 송학현
     * @since : 1.0
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    /**
     * client와 server간 connection 객체를 생성 및 타임아웃 등의 설정을 위한 Bean 설정
     *
     * @return ClientHttpRequestFactory의 구현체 SimpleClientHttpRequestFactory
     * @author : 송학현
     * @since : 1.0
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(1000);
        factory.setBufferRequestBody(false);

        return factory;
    }
}
