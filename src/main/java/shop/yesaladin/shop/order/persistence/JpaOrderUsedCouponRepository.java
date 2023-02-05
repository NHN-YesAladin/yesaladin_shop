package shop.yesaladin.shop.order.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.order.domain.model.OrderCoupon;
import shop.yesaladin.shop.order.domain.model.OrderCoupon.Pk;
import shop.yesaladin.shop.order.domain.repository.CommandOrderCouponRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderCouponRepository;

/**
 * 주문에 사용한 쿠폰 엔티티를 위한 Jpa Repository 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface JpaOrderUsedCouponRepository extends Repository<OrderCoupon, Pk>,
        CommandOrderCouponRepository, QueryOrderCouponRepository {

}
