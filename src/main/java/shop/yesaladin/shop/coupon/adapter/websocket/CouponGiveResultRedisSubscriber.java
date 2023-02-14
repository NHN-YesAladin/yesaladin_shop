package shop.yesaladin.shop.coupon.adapter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.config.SocketProperties;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

/**
 * 레디스 pub/sub 메시지 중 /ws/topic/coupon/give/result 토픽을 구독하다 메시지가 들어오면 websocket으로 발행합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponGiveResultRedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final SocketProperties socketProperties;


    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());

            CouponGiveResultDto parsedMessage = objectMapper.readValue(
                    publishMessage,
                    CouponGiveResultDto.class
            );

            messagingTemplate.convertAndSend(
                    socketProperties.getCouponGiveResultTopicPrefix()
                            + parsedMessage.getRequestId(),
                    parsedMessage
            );

        } catch (Exception e) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "Cannot find message.");
        }
    }
}