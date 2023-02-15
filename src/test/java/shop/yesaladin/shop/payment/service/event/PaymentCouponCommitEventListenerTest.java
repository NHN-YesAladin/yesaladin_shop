package shop.yesaladin.shop.payment.service.event;

import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.payment.dto.PaymentCommitCouponEventDto;

class PaymentCouponCommitEventListenerTest {

    RedisTemplate<String, String> redisTemplate;
    HashOperations<String, Object, Object> hashOperations;
    ListOperations<String, String> listOperations;
    UseCouponService useCouponService;
    PaymentCouponCommitEventListener paymentCouponCommitEventListener;


    @BeforeEach
    void setUp() {
        redisTemplate = Mockito.mock(RedisTemplate.class);
        useCouponService = Mockito.mock(UseCouponService.class);
        hashOperations = Mockito.mock(HashOperations.class);
        listOperations = Mockito.mock(ListOperations.class);

        paymentCouponCommitEventListener = new PaymentCouponCommitEventListener(
                redisTemplate,
                useCouponService
        );
    }

    @Test
    void handlePendingToUsedStatus() {
        // given
        PaymentCommitCouponEventDto eventDto = new PaymentCommitCouponEventDto("OrderNumber");
        String requestId = "temp" + eventDto.getOrderNumber();

        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(hashOperations.get(Mockito.any(), Mockito.any()))
                .thenReturn(requestId);

        Mockito.when(useCouponService.useCoupon(Mockito.any())).thenReturn(new ArrayList<>());

        Mockito.when(hashOperations.delete(Mockito.any(), Mockito.any())).thenReturn(2L);

        // when
        // then
        Assertions.assertThatCode(() -> paymentCouponCommitEventListener.handlePendingToUsedStatus(
                eventDto)).doesNotThrowAnyException();
    }

}
