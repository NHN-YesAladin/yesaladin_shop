package shop.yesaladin.shop.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.adapter.websocket.CouponGiveResultRedisPublisher;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveResultMessageRepository;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveSocketConnectionRepository;
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
    private final CouponGiveResultMessageRepository couponGiveResultMessageRepository;
    private final CouponGiveSocketConnectionRepository couponGiveSocketConnectionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void trySendGiveCouponResultMessage(CouponGiveResultDto resultDto) {
        if (canSendMessage(resultDto)) {
            couponGiveResultRedisPublisher.publish(resultDto);
            couponGiveSocketConnectionRepository.delete(resultDto.getRequestId());
            couponGiveResultMessageRepository.deleteByRequestId(resultDto.getRequestId());
            return;
        }
        couponGiveResultMessageRepository.save(resultDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerConnection(String requestId) {
        couponGiveSocketConnectionRepository.save(requestId);
        if (couponGiveResultMessageRepository.existsByRequestId(requestId)) {
            CouponGiveResultDto result = couponGiveResultMessageRepository.getByRequestId(requestId);
            trySendGiveCouponResultMessage(result);
        }
    }

    private boolean canSendMessage(CouponGiveResultDto resultDto) {
        return couponGiveSocketConnectionRepository.existsByRequestId(resultDto.getRequestId());
    }
}
