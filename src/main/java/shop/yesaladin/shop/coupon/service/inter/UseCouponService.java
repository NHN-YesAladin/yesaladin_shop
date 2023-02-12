package shop.yesaladin.shop.coupon.service.inter;

import java.util.List;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.dto.CouponCodeOnlyDto;

/**
 * 쿠폰 사용과 관련된 기능을 제공하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface UseCouponService {

    /**
     * 쿠폰 사용 요청에 대한 응답 메시지를 받아 쿠폰을 사용합니다.
     *
     * @param message 쿠폰 사용 요청에 대한 응답
     * @return 사용된 쿠폰 코드 리스트
     */
    List<CouponCodeOnlyDto> useCoupon(CouponUseRequestResponseMessage message);
}
