package shop.yesaladin.shop.coupon.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;
import shop.yesaladin.shop.message.Message;

/**
 * 쿠폰 발행 요청을 위한 메시지 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class CouponIssuanceRequestMessage implements Message {

    private final String requestId;
    private final TriggerTypeCode triggerType;
    private final Long couponId;
}
