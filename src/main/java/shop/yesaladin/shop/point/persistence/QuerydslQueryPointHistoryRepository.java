package shop.yesaladin.shop.point.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.model.querydsl.QPointHistory;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;

/**
 * 포인트 내역 조회 관련 repository 구현체 입니다.
 *
 * @author 최에린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QuerydslQueryPointHistoryRepository implements QueryPointHistoryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PointHistory> findById(long id) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        return Optional.ofNullable(queryFactory.select(pointHistory)
                .from(pointHistory)
                .where(pointHistory.id.eq(id))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PointHistory> findByLoginId(
            String loginId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        return queryFactory.select(pointHistory)
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.createDateTime.between(
                                startDate.atStartOfDay(),
                                endDate.plusDays(1).atStartOfDay()
                        )))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PointHistory> findByPointCode(
            PointCode pointCode,
            LocalDate startDate,
            LocalDate endDate
    ) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        return queryFactory.select(pointHistory)
                .from(pointHistory)
                .where(pointHistory.pointCode.eq(pointCode)
                        .and(pointHistory.createDateTime.between(
                                startDate.atStartOfDay(),
                                endDate.plusDays(1).atStartOfDay()
                        )))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMemberPointByLoginId(String loginId) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        Optional<PointHistory> lastPointHistory = Optional.ofNullable(queryFactory.select(
                        pointHistory)
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.pointCode.eq(PointCode.SUM)))
                .orderBy(pointHistory.createDateTime.desc())
                .fetchFirst());

        NumberExpression<Long> expression = pointHistory.pointCode.when(PointCode.SAVE)
                .then(pointHistory.amount)
                .otherwise(0L)
                .sum()
                .subtract(
                        pointHistory.pointCode.when(PointCode.USE)
                                .then(pointHistory.amount)
                                .otherwise(0L)
                                .sum());

        if (lastPointHistory.isPresent()) {
            long curPoint = lastPointHistory.get().getAmount();
            LocalDateTime lastUpdateDate = lastPointHistory.get().getCreateDateTime();

            return curPoint + Optional.ofNullable(queryFactory.select(expression)
                    .from(pointHistory)
                    .where(pointHistory.member.loginId.eq(loginId)
                            .and(pointHistory.createDateTime.after(lastUpdateDate)
                                    .or(pointHistory.createDateTime.eq(lastUpdateDate))))
                    .fetchFirst()).orElse(0L);
        }
        return Optional.ofNullable(queryFactory.select(expression)
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)).fetchFirst()).orElse(0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointHistoryResponseDto> getByLoginIdAndPointCode(
            String loginId,
            PointCode pointCode,
            Pageable pageable
    ) {
        QPointHistory pointHistory = QPointHistory.pointHistory;
        List<PointHistoryResponseDto> content = queryFactory.select(Projections.constructor(
                        PointHistoryResponseDto.class,
                        pointHistory.id,
                        pointHistory.amount,
                        pointHistory.createDateTime,
                        pointHistory.pointCode,
                        pointHistory.pointReasonCode
                ))
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.pointCode.eq(pointCode)))
                .orderBy(pointHistory.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(pointHistory.count())
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.pointCode.eq(pointCode)))
                .fetchFirst();

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointHistoryResponseDto> getByLoginId(String loginId, Pageable pageable) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        List<PointHistoryResponseDto> content = queryFactory.select(Projections.constructor(
                        PointHistoryResponseDto.class,
                        pointHistory.id,
                        pointHistory.amount,
                        pointHistory.createDateTime,
                        pointHistory.pointCode,
                        pointHistory.pointReasonCode
                ))
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.pointCode.ne(PointCode.SUM)))
                .orderBy(pointHistory.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(pointHistory.count())
                .from(pointHistory)
                .where(pointHistory.member.loginId.eq(loginId)
                        .and(pointHistory.pointCode.ne(PointCode.SUM)))
                .fetchFirst();

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointHistoryResponseDto> getByPointCode(PointCode pointCode, Pageable pageable) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        List<PointHistoryResponseDto> content = queryFactory.select(Projections.constructor(
                        PointHistoryResponseDto.class,
                        pointHistory.id,
                        pointHistory.amount,
                        pointHistory.createDateTime,
                        pointHistory.pointCode,
                        pointHistory.pointReasonCode
                ))
                .from(pointHistory)
                .where(pointHistory.pointCode.eq(pointCode))
                .orderBy(pointHistory.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(pointHistory.count())
                .from(pointHistory)
                .where(pointHistory.pointCode.eq(pointCode))
                .fetchFirst();

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointHistoryResponseDto> getBy(Pageable pageable) {
        QPointHistory pointHistory = QPointHistory.pointHistory;
        List<PointHistoryResponseDto> content = queryFactory.select(Projections.constructor(
                        PointHistoryResponseDto.class,
                        pointHistory.id,
                        pointHistory.amount,
                        pointHistory.createDateTime,
                        pointHistory.pointCode,
                        pointHistory.pointReasonCode
                ))
                .from(pointHistory)
                .where(pointHistory.pointCode.ne(PointCode.SUM))
                .orderBy(pointHistory.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(pointHistory.count())
                .where(pointHistory.pointCode.ne(PointCode.SUM))
                .from(pointHistory)
                .fetchFirst();

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount);
    }
}
