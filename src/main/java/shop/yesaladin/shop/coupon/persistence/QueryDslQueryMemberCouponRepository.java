package shop.yesaladin.shop.coupon.persistence;


import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.model.querydsl.QMemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;

/**
 * 멤버가 보유한 쿠폰을 조회하는 레포지토리 인터페이스의 QueryDsl 구현체입니다.
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberCouponRepository implements QueryMemberCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByMemberAndCouponGroupCodeList(
            String memberId,
            List<String> couponGroupCodeList
    ) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;

        return queryFactory.select(memberCoupon.id)
                .from(memberCoupon)
                .where(memberCoupon.member.loginId.eq(memberId))
                .where(memberCoupon.couponGroupCode.in(couponGroupCodeList))
                .fetchFirst() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberCoupon> findByCouponCodes(List<String> couponCodes) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;

        return queryFactory.select(memberCoupon)
                .from(memberCoupon)
                .where(memberCoupon.couponCode.in(couponCodes))
                .fetch();
    }
}
