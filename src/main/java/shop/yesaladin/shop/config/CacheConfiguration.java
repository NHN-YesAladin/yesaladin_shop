package shop.yesaladin.shop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 캐시 처리를 위한 cache 설정
 *
 * @author 배수한
 * @since 1.0
 */
@RequiredArgsConstructor
@EnableCaching
@Configuration
public class CacheConfiguration {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    /**
     * 레디스 캐시를 사용
     *
     * @return 레디스 캐시 매니저
     */
    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("Cache:")
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        springCacheRedisSerializer()));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
    }

    /**
     * localDateTime 형식을 serializing 하기 위해 objectMapper 등록
     *
     * @return objectMapper가 연결된 serializer
     * @author : 배수한
     * @since : 1.0
     */
    @Bean
    public RedisSerializer<Object> springCacheRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

}
