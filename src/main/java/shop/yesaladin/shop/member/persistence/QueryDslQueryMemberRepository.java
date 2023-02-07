package shop.yesaladin.shop.member.persistence;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.querydsl.QMember;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;

/**
 * 회원 조회 관련 QueryDsl Repository 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
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
    public Page<MemberManagerResponseDto> findMemberManagersByLoginId(
            String loginId,
            Pageable pageable
    ) {
        QMember member = QMember.member;
        List<Member> list = queryFactory.selectFrom(member)
                .where(member.loginId.contains(loginId).and(member.isWithdrawal.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(member.count())
                .from(member)
                .where(member.loginId.contains(loginId).and(member.isWithdrawal.isFalse()))
                .fetchFirst();
        return new PageImpl<>(list.stream()
                .map(MemberManagerResponseDto::fromEntity)
                .collect(Collectors.toList()),pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberManagerResponseDto> findMemberManagersByNickname(
            String nickname,
            Pageable pageable
    ) {
        QMember member = QMember.member;
        List<Member> list = queryFactory.selectFrom(member)
                .where(member.nickname.contains(nickname).and(member.isWithdrawal.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(member.count())
                .from(member)
                .where(member.nickname.contains(nickname).and(member.isWithdrawal.isFalse()))
                .fetchFirst();
        return new PageImpl<>(list.stream()
                .map(MemberManagerResponseDto::fromEntity)
                .collect(Collectors.toList()),pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberManagerResponseDto> findMemberManagersByPhone(
            String phone,
            Pageable pageable
    ) {
        QMember member = QMember.member;
        List<Member> list = queryFactory.selectFrom(member)
                .where(member.phone.contains(phone).and(member.isWithdrawal.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(member.count())
                .from(member)
                .where(member.phone.contains(phone).and(member.isWithdrawal.isFalse()))
                .fetchFirst();
        return new PageImpl<>(list.stream()
                .map(MemberManagerResponseDto::fromEntity)
                .collect(Collectors.toList()),pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberManagerResponseDto> findMemberManagersByName(String name, Pageable pageable) {
        QMember member = QMember.member;
        List<Member> list = queryFactory.selectFrom(member)
                .where(member.name.contains(name).and(member.isWithdrawal.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(member.count())
                .from(member)
                .where(member.name.contains(name).and(member.isWithdrawal.isFalse()))
                .fetchFirst();
        return new PageImpl<>(list.stream()
                .map(MemberManagerResponseDto::fromEntity)
                .collect(Collectors.toList()),pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberManagerResponseDto> findMemberManagersBySignUpDate(
            LocalDate signUpDate,
            Pageable pageable
    ) {
        QMember member = QMember.member;
        List<Member> list = queryFactory.selectFrom(member)
                .where(member.signUpDate.eq(signUpDate).and(member.isWithdrawal.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(member.count())
                .from(member)
                .where(member.signUpDate.eq(signUpDate).and(member.isWithdrawal.isFalse()))
                .fetchFirst();
        return new PageImpl<>(list.stream()
                .map(MemberManagerResponseDto::fromEntity)
                .collect(Collectors.toList()),pageable, count);
    }


    /**
     * {@inheritDoc}
     */
    public List<MemberIdDto> findMemberIdsByBirthday(int month, int date) {
        QMember member = QMember.member;
        return queryFactory.select(Projections.constructor(MemberIdDto.class, member.id))
                .from(member)
                .where(member.birthMonth.eq(month), member.birthDay.eq(date))
                .fetch();
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
    public Optional<MemberOrderSheetResponseDto> getMemberOrderData(String loginId) {
        QMember member = QMember.member;
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        Expression<String> memberDefaultAddress = queryFactory.select(memberAddress.address)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.isDefault.isTrue()
                                .and(memberAddress.isDeleted.isFalse())));

        return Optional.ofNullable(queryFactory.select(
                        Projections.constructor(
                                MemberOrderSheetResponseDto.class,
                                member.name,
                                member.phone,
                                memberDefaultAddress
                        ))
                .from(member)
                .where(member.loginId.eq(loginId))
                .fetchFirst());
    }
}
