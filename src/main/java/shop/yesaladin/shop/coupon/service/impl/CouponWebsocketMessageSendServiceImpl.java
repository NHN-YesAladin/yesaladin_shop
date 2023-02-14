package shop.yesaladin.shop.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.adapter.websocket.CouponGiveResultRedisPublisher;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;
import shop.yesaladin.shop.coupon.service.inter.CouponWebsocketMessageSendService;

/**
 * 웹소켓을 사용하여 쿠폰 관련 메시지를 발송하는 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponWebsocketMessageSendServiceImpl implements CouponWebsocketMessageSendService {

    private final CouponGiveResultRedisPublisher couponGiveResultRedisPublisher;

    /**
     * 쿠폰 지급 결과 메시지를 클라이언트로 전송합니다.
     *
     * @param resultDto 전송할 메시지
     */
    public void sendGiveCouponResultMessage(CouponGiveResultDto resultDto) {
        couponGiveResultRedisPublisher.publish(resultDto);
    }
}
