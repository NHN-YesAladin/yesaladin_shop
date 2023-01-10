package shop.yesaladin.shop.order.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.QOrder;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;

/**
 * 주문 데이터 조회를 위한 레포지토리의 QueryDsl 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslOrderQueryRepository implements QueryOrderRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findById(Long id) {
        QOrder order = QOrder.order;

        Optional<OrderCode> orderCode = Optional.ofNullable(queryFactory.select(order.orderCode)
                .from(order)
                .where(order.id.eq(id))
                .fetchFirst());

        if (orderCode.isEmpty()) {
            return Optional.empty();
        }

        PathBuilder<? extends Order> orderDetails = new PathBuilder<>(
                orderCode.get().getOrderClass(),
                "order"
        );

        return Optional.ofNullable(queryFactory.select(orderDetails)
                .from(orderDetails)
                .where(orderDetails.get("id").eq(id))
                .fetchFirst());
    }

    @Override
    public List<OrderSummaryDto> findAllOrdersInPeriod(
            LocalDate startDate, LocalDate endDate, int size, int page
    ) {
        QOrder order = QOrder.order;
        return queryFactory.select(Projections.constructor(
                        OrderSummaryDto.class,
                        order.orderNumber,
                        order.orderDateTime,
                        order.orderCode
                ))
                .from(order)
                .where(order.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                ))
                .offset((long) size * (page - 1))
                .limit(size)
                .fetch();
    }

    @Override
    public long getCountOfOrdersInPeriod(LocalDate startDate, LocalDate endDate) {
        QOrder order = QOrder.order;
        return queryFactory.select(order.count()).from(order).where(order.orderDateTime.between(
                LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
        )).fetchFirst();
    }
}