package shop.yesaladin.shop.member.event;

import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignUpEventHandler implements ApplicationListener<SignUpEvent> {

    private final GiveCouponService giveCouponService;

    @Override
    public void onApplicationEvent(@NonNull SignUpEvent event) {
        try {
            giveCouponService.sendCouponGiveRequest(
                    event.getMemberId(),
                    TriggerTypeCode.SIGN_UP,
                    null, LocalDateTime.now()
            );
            log.info(
                    "Sign up coupon give request message published. Member id : {}",
                    event.getMemberId()
            );
        } catch (Exception e) {
            log.error("Fail to give sign up coupon. memberId : {}", event.getMemberId(), e);
        }
    }
}
