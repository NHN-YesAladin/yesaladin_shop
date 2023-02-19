package shop.yesaladin.shop.coupon.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import shop.yesaladin.coupon.message.CouponResultDto;

/**
 * 쿠폰 관련 요청이 처리 완료되면 발행하는 이벤트 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
public class CouponRequestProcessEndEvent extends ApplicationEvent {

    private final CouponResultDto resultMessage;

    public CouponRequestProcessEndEvent(Object source, CouponResultDto resultMessage) {
        super(source);
        this.resultMessage = resultMessage;
    }
}
