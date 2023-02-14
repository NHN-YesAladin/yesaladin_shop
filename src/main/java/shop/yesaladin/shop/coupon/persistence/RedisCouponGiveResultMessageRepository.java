package shop.yesaladin.shop.coupon.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveResultMessageRepository;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

@RequiredArgsConstructor
@Repository
public class RedisCouponGiveResultMessageRepository implements CouponGiveResultMessageRepository {

    private static final String KEY = "coupon_give_result_message";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void save(CouponGiveResultDto result) {
        try {
            String serializedResult = objectMapper.writeValueAsString(result);
            redisTemplate.opsForHash().put(KEY, result.getRequestId(), serializedResult);
        } catch (JsonProcessingException e) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "Cannot serialize message.");
        }
    }

    @Override
    public boolean existsByRequestId(String requestId) {
        return Objects.nonNull(redisTemplate.opsForHash().get(KEY, requestId));
    }

    @Override
    public CouponGiveResultDto getByRequestId(String requestId) {
        String stringMessage = (String) redisTemplate.opsForHash().get(KEY, requestId);
        try {
            return objectMapper.readValue(stringMessage, CouponGiveResultDto.class);

        } catch (JsonProcessingException e) {
            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Cannot deserialize message."
            );
        }
    }

    @Override
    public void deleteByRequestId(String requestId) {
        redisTemplate.opsForHash().delete(KEY, requestId);
    }
}
