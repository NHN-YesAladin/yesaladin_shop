package shop.yesaladin.shop.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.querydsl.QMember;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.CustomQueryMemberRepository;
import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;

@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberRepository implements CustomQueryMemberRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberOrderResponseDto getMemberOrderData(String loginId) {
        QMember member = QMember.member;
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return queryFactory.select(
                        Projections.constructor(
                                MemberOrderResponseDto.class,
                                member.name,
                                member.phone,
                                memberAddress.address.nullif("")
                        ))
                .from(member)
                .leftJoin(memberAddress).fetchJoin()
                .on(member.id.eq(memberAddress.member.id))
                .where(member.loginId.eq(loginId).and(memberAddress.isDefault.isTrue()))
                .fetchFirst();
    }
}
