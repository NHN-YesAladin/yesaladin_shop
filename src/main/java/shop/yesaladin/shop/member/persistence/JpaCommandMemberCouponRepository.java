package shop.yesaladin.shop.member.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.repository.CommandMemberCouponRepository;

public interface JpaCommandMemberCouponRepository extends Repository<MemberCoupon, Long>, CommandMemberCouponRepository {

}
