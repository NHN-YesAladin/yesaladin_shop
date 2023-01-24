package shop.yesaladin.shop.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;

/**
 * 회원 배송지의 조회와 관련한 querydsl repository 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberAddressRepository implements QueryMemberAddressRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberAddress> findByMember(Member member) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.eq(member))
                .fetch();
    }

    @Override
    public Optional<MemberAddress> findById(long id) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.id.eq(id))
                .fetchFirst());
    }

    @Override
    public Optional<MemberAddress> getByLoginIdAndMemberAddressId(
            String loginId,
            long memberAddressId
    ) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.id.eq(memberAddressId)))
                .fetchFirst());
    }

    @Override
    public boolean existByLoginIdAndMemberAddressId(String loginId, long memberAddressId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        MemberAddress result = queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.id.eq(memberAddressId)))
                .fetchFirst();

        return Objects.nonNull(result);
    }
}
