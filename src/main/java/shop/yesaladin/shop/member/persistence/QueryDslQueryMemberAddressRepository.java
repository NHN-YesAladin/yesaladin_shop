package shop.yesaladin.shop.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public long countByLoginId(String loginId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return queryFactory.select(memberAddress.count())
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.isDeleted.isFalse()))
                .fetchFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberAddress> findById(long id) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.id.eq(id).and(memberAddress.isDeleted.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberAddress> findByLoginId(String loginId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.isDeleted.isFalse()))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberAddress> findByLoginIdAndMemberAddressId(
            String loginId,
            long memberAddressId
    ) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.id.eq(memberAddressId))
                        .and(memberAddress.isDeleted.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberAddressResponseDto> getById(long id) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(Projections.constructor(
                        MemberAddressResponseDto.class,
                        memberAddress.id,
                        memberAddress.address,
                        memberAddress.isDefault
                ))
                .from(memberAddress)
                .where(memberAddress.id.eq(id).and(memberAddress.isDeleted.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberAddressResponseDto> getByLoginId(String loginId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return queryFactory.select(Projections.constructor(
                        MemberAddressResponseDto.class,
                        memberAddress.id,
                        memberAddress.address,
                        memberAddress.isDefault
                ))
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.isDeleted.isFalse()))
                .orderBy(memberAddress.isDefault.desc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberAddressResponseDto> getByLoginIdAndMemberAddressId(
            String loginId,
            long memberAddressId
    ) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(Projections.constructor(
                        MemberAddressResponseDto.class,
                        memberAddress.id,
                        memberAddress.address,
                        memberAddress.isDefault
                ))
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.id.eq(memberAddressId))
                        .and(memberAddress.isDeleted.isFalse()))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existByLoginIdAndMemberAddressId(String loginId, long memberAddressId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        return Optional.ofNullable(queryFactory.select(memberAddress)
                .from(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.id.eq(memberAddressId))
                        .and(memberAddress.isDeleted.isFalse()))
                .fetchFirst()).isPresent();
    }
}
