package shop.yesaladin.shop.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberCoupon;
import shop.yesaladin.shop.member.domain.repository.QueryMemberCouponRepository;

/**
 * 회원 쿠폰 조회 관련 repository 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberCouponRepository implements QueryMemberCouponRepository {

    private final JPAQueryFactory queryFactory;

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
