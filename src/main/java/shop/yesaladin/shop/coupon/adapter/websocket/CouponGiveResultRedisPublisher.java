package shop.yesaladin.shop.coupon.adapter.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.config.SocketProperties;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

/**
 * 쿠폰 지급 완료 메시지를 redis pub/sub으로 발행합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CouponGiveResultRedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SocketProperties socketProperties;

    public void publish(CouponGiveResultDto result) {
        redisTemplate.convertAndSend(socketProperties.getCouponGiveResultTopicPrefix(), result);
    }
}