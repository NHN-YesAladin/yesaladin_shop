package shop.yesaladin.shop.point.persistence;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.model.querydsl.QPointHistory;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;

/**
 * {@inheritDoc}
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
    public List<PointHistory> findByMemberId(
            long memberId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        return queryFactory.select(pointHistory)
                .from(pointHistory)
                .where(pointHistory.member.id.eq(memberId)
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
    public long getMemberPointByMemberId(long memberId) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        Optional<PointHistory> lastPointHistory = Optional.ofNullable(queryFactory.select(
                        pointHistory)
                .from(pointHistory)
                .where(pointHistory.member.id.eq(memberId)
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
                    .where(pointHistory.member.id.eq(memberId)
                            .and(pointHistory.createDateTime.after(lastUpdateDate)
                                    .or(pointHistory.createDateTime.eq(lastUpdateDate))))
                    .fetchFirst()).orElse(0L);
        }
        return Optional.ofNullable(queryFactory.select(expression)
                .from(pointHistory)
                .where(pointHistory.member.id.eq(memberId)).fetchFirst()).orElse(0L);
    }
}
