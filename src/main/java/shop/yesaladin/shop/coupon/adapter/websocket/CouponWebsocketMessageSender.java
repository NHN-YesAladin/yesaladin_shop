package shop.yesaladin.shop.coupon.adapter.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

@RequiredArgsConstructor
@Component
public class CouponWebsocketMessageSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendGiveCouponResultMessage(CouponGiveResultDto resultDto) {
        messagingTemplate.convertAndSend(
                "/ws/topic/coupon/give/result/" + resultDto.getRequestId(),
                resultDto
        );
    }
}
