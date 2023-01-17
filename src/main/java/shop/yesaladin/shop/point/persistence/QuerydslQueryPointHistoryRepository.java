package shop.yesaladin.shop.point.persistence;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.model.querydsl.QPointHistory;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;

@Repository
@RequiredArgsConstructor
public class QuerydslQueryPointHistoryRepository implements QueryPointHistoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PointHistory> findById(long id) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        return Optional.ofNullable(queryFactory.select(pointHistory)
                .from(pointHistory)
                .where(pointHistory.id.eq(id))
                .fetchFirst());
    }

    @Override
    public long getMemberPointByMemberId(long memberId) {
        QPointHistory pointHistory = QPointHistory.pointHistory;

        Optional<PointHistory> lastPointHistory = Optional.ofNullable(queryFactory.select(
                        pointHistory)
                .from(pointHistory)
                .where(pointHistory.member.id.eq(memberId)
                        .and(pointHistory.pointCode.eq(PointCode.SUM)))
                .orderBy(pointHistory.createDateTime.desc())
                .limit(1)
                .fetchOne());

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

            return curPoint + queryFactory.select(expression)
                    .from(pointHistory)
                    .where(pointHistory.member.id.eq(memberId)
                            .and(pointHistory.createDateTime.after(lastUpdateDate))).fetchFirst();
        } else {
            return queryFactory.select(expression)
                    .from(pointHistory)
                    .where(pointHistory.member.id.eq(memberId)).fetchFirst();
        }
    }
}
