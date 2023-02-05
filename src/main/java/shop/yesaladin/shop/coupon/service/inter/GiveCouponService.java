package shop.yesaladin.shop.coupon.service.inter;

import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;

/**
 * 쿠폰 지급과 관련된 기능을 제공하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface GiveCouponService {

    /**
     * 회원에게 쿠폰을 지급합니다. 쿠폰이 이미 존재하는 경우 지급하지 않습니다.
     * @param triggerTypeCode 발행할 쿠폰의 트리거 타입 코드 (필수값)
     * @param couponId 발행할 쿠폰의 ID (선택값)
     * @throws ClientException 쿠폰이 이미 존재하는 경우
     */
    void sendCouponGiveRequest(String memberId, TriggerTypeCode triggerTypeCode, Long couponId);

    void giveCouponToMember(CouponGiveRequestResponseMessage responseMessage);
}
