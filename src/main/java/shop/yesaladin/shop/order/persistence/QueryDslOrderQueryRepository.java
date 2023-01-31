package shop.yesaladin.shop.order.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.querydsl.QMember;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.querydsl.QMemberOrder;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrder;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.payment.domain.model.querydsl.QPayment;

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

    /**
     * {@inheritDoc}
     *
     */
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

        PathBuilder<? extends Order> orderDetails = new PathBuilder<>(orderCode.get()
                .getOrderClass(), "order");

        return Optional.ofNullable(queryFactory.select(orderDetails)
                .from(orderDetails)
                .where(orderDetails.get("id").eq(id))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Page<OrderSummaryDto> findAllOrdersInPeriod(
            LocalDate startDate, LocalDate endDate, Pageable pageable
    ) {
        QOrder order = QOrder.order;
        List<OrderSummaryDto> data = queryFactory.select(Projections.constructor(
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(order.count())
                .from(order)
                .where(order.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                ));

        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Page<OrderSummaryDto> findAllOrdersInPeriodByMemberId(
            LocalDate startDate, LocalDate endDate, long memberId, Pageable pageable
    ) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        List<OrderSummaryDto> data = queryFactory.select(Projections.constructor(
                        OrderSummaryDto.class,
                        memberOrder.orderNumber,
                        memberOrder.orderDateTime,
                        memberOrder.orderCode
                ))
                .from(memberOrder)
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(memberOrder.count())
                .from(memberOrder)
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )));

        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public long getCountOfOrdersInPeriod(LocalDate startDate, LocalDate endDate) {
        QOrder order = QOrder.order;
        return queryFactory.select(order.count()).from(order).where(order.orderDateTime.between(
                LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
        )).fetchFirst();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public long getCountOfOrdersInPeriodByMemberId(
            LocalDate startDate,
            LocalDate endDate,
            long memberId
    ) {
        QMemberOrder qMemberOrder = QMemberOrder.memberOrder;
        return queryFactory.select(qMemberOrder.count())
                .from(qMemberOrder)
                .where(qMemberOrder.member.id.eq(memberId))
                .where(qMemberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )).fetchFirst();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        QOrder order = QOrder.order;

        Optional<OrderCode> orderCode = Optional.ofNullable(queryFactory.select(order.orderCode)
                .from(order)
                .where(order.orderNumber.eq(orderNumber))
                .fetchFirst());

        if (orderCode.isEmpty()) {
            return Optional.empty();
        }

        PathBuilder<? extends Order> orderDetails = new PathBuilder<>(orderCode.get()
                .getOrderClass(), "order");

        return Optional.ofNullable(queryFactory.select(orderDetails)
                .from(orderDetails)
                .where(orderDetails.get("orderNumber").eq(orderNumber))
                .fetchFirst());
    }

    @Override
    public Page<OrderSummaryResponseDto> findOrdersInPeriodByMemberId(
            LocalDate startDate,
            LocalDate endDate,
            long memberId,
            Pageable pageable
    ) {
        QPayment payment = QPayment.payment;
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        List<OrderSummaryDto> data = queryFactory.select(Projections.constructor(
                        OrderSummaryDto.class,
                        memberOrder.orderNumber,
                        memberOrder.orderDateTime,
                        memberOrder.orderCode
                ))
                .from(memberOrder)
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(memberOrder.count())
                .from(memberOrder)
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )));

//        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
        return null;
    }


}
