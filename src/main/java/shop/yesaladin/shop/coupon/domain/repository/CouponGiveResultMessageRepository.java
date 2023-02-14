package shop.yesaladin.shop.coupon.domain.repository;

import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

/**
 * 쿠폰 지급 결과 메시지를 저장 / 수정 / 삭제하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CouponGiveResultMessageRepository {

    void save(CouponGiveResultDto result);

    boolean existsByRequestId(String requestId);

    CouponGiveResultDto getByRequestId(String requestId);

    void deleteByRequestId(String requestId);
}
