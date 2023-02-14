package shop.yesaladin.shop.coupon.persistence;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
    private final Clock clock;

    @Override
    public boolean existsByMemberAndCouponGroupCodeList(
            String memberId, List<String> couponGroupCodeList
    ) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;

        return queryFactory.select(memberCoupon.id)
                .from(memberCoupon)
                .where(memberCoupon.member.loginId.eq(memberId))
                .where(memberCoupon.couponGroupCode.in(couponGroupCodeList))
                .fetchFirst() != null;
    }

    @Override
    public Page<MemberCoupon> findMemberCouponByMemberId(
            Pageable pageable, String memberId, boolean usable
    ) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
        LocalDate today = LocalDate.now(clock);
        BooleanExpression usableCondition = memberCoupon.isUsed.eq(false)
                .and(memberCoupon.expirationDate.goe(today));
        BooleanExpression unusableCondition = memberCoupon.isUsed.eq(true)
                .or(memberCoupon.expirationDate.before(today));

        List<MemberCoupon> memberCouponList = queryFactory.select(memberCoupon)
                .from(memberCoupon)
                .where(memberCoupon.member.loginId.eq(memberId))
                .where(usable ? usableCondition : unusableCondition)
                .orderBy(memberCoupon.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(memberCoupon.id.count())
                .from(memberCoupon)
                .where(memberCoupon.member.loginId.eq(memberId));

        return PageableExecutionUtils.getPage(memberCouponList, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberCoupon> findByCouponCodes(List<String> couponCodes) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;

        return queryFactory.select(memberCoupon)
                .from(memberCoupon)
                .where(memberCoupon.couponCode.in(couponCodes)
                        .and(memberCoupon.isUsed.isFalse())
                        .and(memberCoupon.expirationDate.before(LocalDate.now(clock))))
                .fetch();
    }
}
