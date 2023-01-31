package shop.yesaladin.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.security.filter.JwtAuthorizationFilter;
import shop.yesaladin.security.provider.JwtTokenAuthenticationProvider;

/**
 * Spring Security 관련 설정 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider(
            RestTemplate restTemplate, GatewayProperties gatewayProperties
    ) {

        return new JwtTokenAuthenticationProvider(restTemplate, gatewayProperties.getAuthUrl());
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(jwtTokenAuthenticationProvider(null, null))
                .build();
    }

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> request
//                        .mvcMatchers(HttpMethod.GET, "/v1/categories/**").permitAll()
//                        .mvcMatchers(HttpMethod.GET, "/v1/members/login/**").permitAll()
//                        .mvcMatchers("/**").authenticated())
                        .mvcMatchers("/**").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        new JwtAuthorizationFilter(authenticationManager(http)),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
