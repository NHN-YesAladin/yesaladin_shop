package shop.yesaladin.shop.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

/**
 * 회원 쿠폰 테이블에 JPA 로 접근 가능한 인터페이스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface JpaCommandMemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

}
