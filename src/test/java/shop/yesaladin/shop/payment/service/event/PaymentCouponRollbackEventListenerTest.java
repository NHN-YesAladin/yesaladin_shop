package shop.yesaladin.shop.payment.service.event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.payment.dto.PaymentCommitCouponEventDto;
import shop.yesaladin.shop.payment.dto.PaymentRollbackCouponEventDto;

/**
 * @author 배수한
 * @since 1.0
 */
class PaymentCouponRollbackEventListenerTest {

    RedisTemplate<String, String> redisTemplate;
    HashOperations<String, Object, Object> hashOperations;
    ListOperations<String, String> listOperations;
    UseCouponService useCouponService;
    PaymentCouponRollbackEventListener paymentCouponRollbackEventListener;


    @BeforeEach
    void setUp() {
        redisTemplate = Mockito.mock(RedisTemplate.class);
        useCouponService = Mockito.mock(UseCouponService.class);
        hashOperations = Mockito.mock(HashOperations.class);
        listOperations = Mockito.mock(ListOperations.class);

        paymentCouponRollbackEventListener = new PaymentCouponRollbackEventListener(redisTemplate,
                useCouponService);
    }

    @Test
    void handleCurrentToNotUsed() {
        // given
        PaymentRollbackCouponEventDto eventDto = new PaymentRollbackCouponEventDto("OrderNumber");
        String requestId = "temp" + eventDto.getOrderNumber();

        List<String> couponCodeList = List.of("C1", "C2");

        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(hashOperations.get(Mockito.any(), Mockito.any()))
                .thenReturn(requestId);

        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);

        Mockito.when(useCouponService.cancelCouponUse(Mockito.any())).thenReturn(new ArrayList<>());

        Mockito.when(hashOperations.delete(Mockito.any(), Mockito.any())).thenReturn(2L);

        // when
        // then
        Assertions.assertThatCode(() -> paymentCouponRollbackEventListener.handleCurrentToNotUsed(
                eventDto)).doesNotThrowAnyException();
    }

    @Test
    void handleCurrentToNotUsed_listIsNull_fail() {
        // given
        PaymentRollbackCouponEventDto eventDto = new PaymentRollbackCouponEventDto("OrderNumber");
        String requestId = "temp" + eventDto.getOrderNumber();

        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(hashOperations.get(Mockito.any(), Mockito.any()))
                .thenReturn(requestId);

        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);
        Mockito.when(listOperations.range(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);

        Mockito.when(useCouponService.cancelCouponUse(Mockito.any())).thenReturn(new ArrayList<>());

        Mockito.when(hashOperations.delete(Mockito.any(), Mockito.any())).thenReturn(2L);

        // when
        // then
        Assertions.assertThatCode(() -> paymentCouponRollbackEventListener.handleCurrentToNotUsed(
                        eventDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Request id not exists or expired. Request id : ");
    }
}
