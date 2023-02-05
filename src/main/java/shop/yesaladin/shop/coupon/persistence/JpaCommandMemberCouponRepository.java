package shop.yesaladin.shop.coupon.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;

/**
 * 회원이 가진 쿠폰을 추가/삭제하는 레포지토리 인터페이스의 JPA 구현체입니댜.
 *
 * @author 김홍대
 * @since 1.0
 */
public interface JpaCommandMemberCouponRepository extends Repository<MemberCoupon, Long>,
        CommandMemberCouponRepository {

}
