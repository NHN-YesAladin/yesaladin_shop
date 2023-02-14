package shop.yesaladin.shop.coupon.domain.repository;

/**
 * 쿠폰 지급 관련 소켓 연결 정보를 저장 / 조회 / 삭제하는 레포지토리 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface CouponGiveSocketConnectionRepository {

    void save(String requestId);

    boolean existsByRequestId(String requestId);

    void delete(String requestId);

}
