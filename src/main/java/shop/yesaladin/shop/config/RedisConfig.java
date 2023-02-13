package shop.yesaladin.shop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 클래스 입니다.
 *
 * @author : 배수한
 * @since : 1.0
 */

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final ObjectMapper objectMapper;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * Redis Connection 설정 Bean 입니다.
     *
     * @return Redis Connection 설정이 들어간 Factory
     * @author : 배수한
     * @since : 1.0
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(password);
        configuration.setDatabase(database);

        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Redis에 key, value 등과 관련된 연산을 하기 위해 설정합니다.
     *
     * @return Redis에 get, put 등을 하기 위한 RedisTemplate
     * @author : 배수한
     * @since : 1.0
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(springDefaultRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(springDefaultRedisSerializer());

        return redisTemplate;
    }

    /**
     * localDateTime 형식을 serializing 하기 위해 objectMapper 등록
     *
     * @return objectMapper가 연결된 serializer
     * @author : 배수한
     * @since : 1.0
     */
    @Bean
    public RedisSerializer<Object> springDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }


}
