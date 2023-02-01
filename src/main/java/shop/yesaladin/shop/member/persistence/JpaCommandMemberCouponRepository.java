package shop.yesaladin.shop.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

public interface JpaCommandMemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

}
