package shop.yesaladin.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.yesaladin.shop.interceptor.RequestLoggingInterceptor;

/**
 * Web MVC 관련 설정 클래스입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggingInterceptor());
    }
}
