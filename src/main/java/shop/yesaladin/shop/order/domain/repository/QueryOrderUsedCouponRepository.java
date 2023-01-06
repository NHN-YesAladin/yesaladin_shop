package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon.Pk;

public interface QueryOrderUsedCouponRepository {

    Optional<OrderUsedCoupon> findById(Pk pk);

}
