package shop.yesaladin.shop.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.querydsl.QMember;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;

/**
 * 회원 조회 관련 QueryDsl Repository 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberRepository implements QueryMemberRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Member> findById(Long id) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.id.eq(id).and(member.isWithdrawal.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Member> findMemberByNickname(String nickname) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname).and(member.isWithdrawal.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Member> findMemberByLoginId(String loginId) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.loginId.eq(loginId).and(member.isWithdrawal.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Member> findMemberByEmail(String email) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.email.eq(email).and(member.isWithdrawal.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMemberByLoginId(String loginId) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.loginId.eq(loginId).and(member.isWithdrawal.isFalse()))
                .fetchFirst()).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMemberByNickname(String nickname) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname).and(member.isWithdrawal.isFalse()))
                .fetchFirst()).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMemberByEmail(String email) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.email.eq(email).and(member.isWithdrawal.isFalse()))
                .fetchFirst()).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMemberByPhone(String phone) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.phone.eq(phone).and(member.isWithdrawal.isFalse()))
                .fetchFirst()).isPresent();
    }

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