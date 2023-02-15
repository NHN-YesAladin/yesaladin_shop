package shop.yesaladin.shop.payment.service.event;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.payment.dto.PaymentCommitCouponEventDto;

/**
 * 결제 진행 중 쿠폰의 메세지 발급이 커밋 상황에 따라 동작하도록하는 이벤트 리스너
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentCouponCommitEventListener {

    private static final String REQUEST_KEY = "USE_COUPON_REQ_ID";
    private final RedisTemplate<String, String> redisTemplate;
    private final UseCouponService useCouponService;

    /**
     * 결제 service가 정상동작(commit 됨)의 경우, 쿠폰을 사용대기 -> 사용으로 상태 변경
     * <p>
     * fallbackExecution = true : 트랜잭션이 있던 없던 해당 이벤트를 실행하겠다는 의미 : 해당 메서드는 카프카, 레디스를 사용하기 때문에 필요 없음
     * </p>
     *
     * @param eventDto 주문 번호가 담긴 event dto
     */
    @EventListener
    public void handlePendingToUsedStatus(PaymentCommitCouponEventDto eventDto) {
        log.info("PaymentCouponEventListener - handlePendingToUsedStatus");
        String useCouponReqId = getRequestIdForCouponsToRedis(eventDto.getOrderNumber());
        useCouponService.useCoupon(CouponUseRequestResponseMessage.builder()
                .requestId(useCouponReqId)
                .success(true)
                .build());

        // 레디스에서 주문번호로 삭제
        deleteRequestIdForCouponsToRedis(useCouponReqId);
        log.info("handlePendingToUsedStatus success");
    }


    private String getRequestIdForCouponsToRedis(String orderNumber) {
        //TODO test용이라 put 지워야함
        redisTemplate.opsForHash().put(REQUEST_KEY, orderNumber, "temp" + orderNumber);
        return Objects.requireNonNull(redisTemplate.opsForHash().get(REQUEST_KEY, orderNumber))
                .toString();
    }

    private void deleteRequestIdForCouponsToRedis(String orderNumber) {
        redisTemplate.opsForHash().delete(REQUEST_KEY, orderNumber);
    }
}
