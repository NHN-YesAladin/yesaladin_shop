package shop.yesaladin.shop.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.adapter.websocket.CouponResultRedisPublisher;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveResultMessageRepository;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveSocketConnectionRepository;
import shop.yesaladin.shop.coupon.dto.CouponResultDto;
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

    private final CouponResultRedisPublisher couponResultRedisPublisher;
    private final CouponGiveResultMessageRepository couponGiveResultMessageRepository;
    private final CouponGiveSocketConnectionRepository couponGiveSocketConnectionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void trySendGiveCouponResultMessage(CouponResultDto resultDto) {
        if (canSendMessage(resultDto)) {
            couponResultRedisPublisher.publish(resultDto);
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
            CouponResultDto result = couponGiveResultMessageRepository.getByRequestId(requestId);
            trySendGiveCouponResultMessage(result);
        }
    }

    private boolean canSendMessage(CouponResultDto resultDto) {
        return couponGiveSocketConnectionRepository.existsByRequestId(resultDto.getRequestId());
    }
}
