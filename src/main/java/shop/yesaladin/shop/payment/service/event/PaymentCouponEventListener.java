package shop.yesaladin.shop.payment.service.event;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;
import shop.yesaladin.shop.payment.dto.PaymentCouponEventDto;
import shop.yesaladin.shop.payment.dto.PaymentEventDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;

/**
 * 결제 진행 중 쿠폰의 메세지 발급이 커밋, 롤백 상황에 따라 동작하도록하는 이벤트 리스너
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentCouponEventListener {
    private final RedisTemplate<String, String> redisTemplate;
    private final UseCouponService useCouponService;
    private static final String REQUEST_KEY = "USE_COUPON_REQ_ID";

    /**
     * 결제 service가 정상동작(commit 됨)의 경우, 쿠폰을 사용대기 -> 사용으로 상태 변경
     *
     * @param eventDto 주문 번호가 담긴 event dto
     */
    @TransactionalEventListener
    public void handlePendingToUsedStatus(PaymentCouponEventDto eventDto) {
        String useCouponReqId = getRequestIdForCouponsToRedis(eventDto.getOrderNumber());
        useCouponService.useCoupon(CouponUseRequestResponseMessage.builder()
                .requestId(useCouponReqId)
                .success(true)
                .build());

        // 레디스에서 주문번호로 삭제
        deleteRequestIdForCouponsToRedis(useCouponReqId);
        log.info("handlePendingToUsedStatus success");
    }

    /**
     * 결제 service가 실패된(rollback 됨) 경우, 쿠폰을 (사용대기 & 사용) -> 미사용으로 상태 변경
     *
     * @param eventDto 주문 번호가 담긴 event dto
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleCurrentToNotUsed(PaymentCouponEventDto eventDto) {
        String orderNumber = eventDto.getOrderNumber();
        String useCouponReqId = getRequestIdForCouponsToRedis(orderNumber);

        //TODO useCouponService.cancelCoupon()
        List<String> usedCouponCode = getUsedCouponCode(orderNumber);

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
