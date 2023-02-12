package shop.yesaladin.shop.coupon.adapter.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

/**
 * 웹소켓을 사용하여 쿠폰 관련 메시지를 발송하는 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponWebsocketMessageSender {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 쿠폰 지급 결과 메시지를 클라이언트로 전송합니다.
     *
     * @param resultDto 전송할 메시지
     */
    public void sendGiveCouponResultMessage(CouponGiveResultDto resultDto) {
        messagingTemplate.convertAndSend(
                "/ws/topic/coupon/give/result/" + resultDto.getRequestId(),
                resultDto
        );
    }
}
