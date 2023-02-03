package shop.yesaladin.shop.member.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.querydsl.QMember;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;

/**
 * 회원 조회 관련 QueryDsl Repository 구현체 입니다.
 *
 * @author 송학현
 * @author 김선홍
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
    public Optional<Member> findMemberByPhone(String phone) {
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.phone.eq(phone).and(member.isWithdrawal.isFalse()))
                .fetchFirst());
    }

    @Override
    public Page<Member> findMemberByName(String name, int offset, int limit) {
        QMember member = QMember.member;
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
        JPAQuery<Long> countQuery = queryFactory.select(member.count())
                .from(member)
                .where(member.name.eq(name));

        return PageableExecutionUtils.getPage(
                memberList,
                PageRequest.of(Math.toIntExact(offset), Math.toIntExact(limit)),
                countQuery::fetchOne
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Member> findMemberBySignUpDate(LocalDate signUpDate, int offset, int limit) {
        QMember member = QMember.member;
        List<Member> memberList = queryFactory.selectFrom(member)
                .where(member.signUpDate.eq(signUpDate))
                .offset(offset)
                .limit(limit)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(member.count())
                .from(member)
                .where(member.signUpDate.eq(signUpDate));

        return PageableExecutionUtils.getPage(
                memberList,
                PageRequest.of(Math.toIntExact(offset), Math.toIntExact(limit)),
                countQuery::fetchOne
        );
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
}
