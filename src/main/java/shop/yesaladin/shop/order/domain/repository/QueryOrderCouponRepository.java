package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.OrderCoupon;
import shop.yesaladin.shop.order.domain.model.OrderCoupon.Pk;

/**
 * 주문에 사용한 쿠폰 조회 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderCouponRepository {

    /**
     * pk를 통해 주문에 사용한 쿠폰 데이터를 조회합니다.
     *
     * @param pk pk
     * @return 조회된 주문에 사용한 쿠폰 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<OrderCoupon> findById(Pk pk);

}
