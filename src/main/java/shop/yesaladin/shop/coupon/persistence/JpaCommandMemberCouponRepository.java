package shop.yesaladin.shop.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;

public interface JpaCommandMemberCouponRepository extends Repository<MemberCoupon, Long>,
        CommandMemberCouponRepository {

}
