package shop.yesaladin.shop.payment.service.event;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.payment.dto.PaymentRollbackCouponEventDto;

/**
 * 결제 진행 중 쿠폰의 메세지 발급이 롤백 상황에 따라 동작하도록하는 이벤트 리스너
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentCouponRollbackEventListener {

    private static final String REQUEST_KEY = "USE_COUPON_REQ_ID";
    private final RedisTemplate<String, String> redisTemplate;
    private final UseCouponService useCouponService;

    /**
     * 결제 service가 실패된(rollback 됨) 경우, 쿠폰을 (사용대기 & 사용) -> 미사용으로 상태 변경
     *
     * @param eventDto 주문 번호가 담긴 event dto
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleCurrentToNotUsed(PaymentRollbackCouponEventDto eventDto) {
        log.info("PaymentCouponEventListener - handleCurrentToNotUsed");
        String orderNumber = eventDto.getOrderNumber();
        String useCouponReqId = getRequestIdForCouponsToRedis(orderNumber);

        List<String> usedCouponCodes = getUsedCouponCode(orderNumber);
        useCouponService.cancelCouponUse(usedCouponCodes);

        // 레디스에서 주문번호로 삭제
        deleteRequestIdForCouponsToRedis(useCouponReqId);
        log.info("handleCurrentToNotUsed success");
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

    private List<String> getUsedCouponCode(String requestId) {
        List<String> couponCodeList = redisTemplate.opsForList().range(requestId, 0, -1);
        if (Objects.isNull(couponCodeList)) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Request id not exists or expired. Request id : " + requestId
            );
        }
        return couponCodeList;
    }

}
