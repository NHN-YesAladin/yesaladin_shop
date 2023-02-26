package shop.yesaladin.shop.order.persistence;

import com.querydsl.core.types.ExpressionUtils;
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
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.querydsl.QMemberOrder;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrder;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrderProduct;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;

/**
 * 주문 데이터 조회를 위한 레포지토리의 QueryDsl 구현체입니다.
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslOrderQueryRepository implements QueryOrderRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
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

    @Override
    public Optional<MemberOrder> findMemberOrderByIdAndLoginId(Long id, String loginId) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;

        return Optional.ofNullable(queryFactory.select(memberOrder)
                .from(memberOrder)
                .where(memberOrder.id.eq(id).and(memberOrder.member.loginId.eq(loginId)))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
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
                        LocalDateTime.of(endDate.plusDays(1), LocalTime.MIDNIGHT)
                ));

        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
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
                        LocalDateTime.of(endDate.plusDays(1), LocalTime.MIDNIGHT)
                )));

        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrderSummaryResponseDto> findOrdersInPeriodByMemberId(
            LocalDate startDate,
            LocalDate endDate,
            long memberId,
            Pageable pageable
    ) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QOrderStatusChangeLog orderStatusChangeLog = QOrderStatusChangeLog.orderStatusChangeLog;

        List<OrderSummaryResponseDto> data = queryFactory.select(Projections.constructor(
                        OrderSummaryResponseDto.class,
                        memberOrder.id,
                        memberOrder.orderNumber,
                        memberOrder.orderDateTime,
                        memberOrder.name,
                        memberOrder.totalAmount,
                        ExpressionUtils.as(queryFactory.select(orderStatusChangeLog.orderStatusCode.max())
                                .from(orderStatusChangeLog)
                                .where(orderStatusChangeLog.order.id.eq(memberOrder.id))
                                .groupBy(orderStatusChangeLog.order.id), "orderStatusCode"),
                        memberOrder.member.id,
                        memberOrder.member.name,
                        orderProduct.count(),
                        orderProduct.quantity.sum(),
                        memberOrder.orderCode
                ))
                .from(memberOrder)
                .leftJoin(orderProduct)
                .on(memberOrder.id.eq(orderProduct.order.id))
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate.plusDays(1), LocalTime.MIDNIGHT)
                )))
                .where(memberOrder.isHidden.isFalse())
                .groupBy(memberOrder.id)
                .orderBy(memberOrder.orderDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalCount = queryFactory.select(memberOrder.count())
                .from(memberOrder)
                .where(memberOrder.member.id.eq(memberId).and(memberOrder.orderDateTime.between(
                        LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                        LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
                )))
                .where(memberOrder.isHidden.isFalse())
                .groupBy(memberOrder.id)
                .fetch().size();
        return PageableExecutionUtils.getPage(data, pageable, () -> totalCount);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OrderPaymentResponseDto> findPaymentDtoByMemberOrderId(long orderId) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        return Optional.ofNullable(queryFactory.select(Projections.constructor(
                        OrderPaymentResponseDto.class,
                        memberOrder.member.name,
                        memberOrder.memberAddress.address
                ))
                .from(memberOrder)
                .where(memberOrder.id.eq(orderId))
                .fetchFirst());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrderStatusResponseDto> findOrderStatusResponsesByLoginIdAndStatus(
            String loginId,
            OrderStatusCode code,
            Pageable pageable
    ) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        QOrderStatusChangeLog orderStatusChangeLog = QOrderStatusChangeLog.orderStatusChangeLog;

        List<OrderStatusResponseDto> data = queryFactory.select(Projections.constructor(
                        OrderStatusResponseDto.class,
                        memberOrder.id,
                        memberOrder.orderNumber,
                        memberOrder.orderDateTime,
                        memberOrder.name,
                        memberOrder.totalAmount,
                        memberOrder.member.loginId,
                        memberOrder.recipientName,
                        memberOrder.orderCode
                ))
                .from(memberOrder)
                .leftJoin(orderStatusChangeLog)
                .on(memberOrder.id.eq(orderStatusChangeLog.order.id))
                .where(memberOrder.member.loginId.eq(loginId))
                .groupBy(memberOrder.id)
                .having(orderStatusChangeLog.orderStatusCode.max().eq(code))
                .having(orderStatusChangeLog.orderStatusCode.max().lt(OrderStatusCode.REFUND))
                .orderBy(memberOrder.orderDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalCount = queryFactory.select(memberOrder.count())
                .from(memberOrder)
                .leftJoin(orderStatusChangeLog)
                .on(memberOrder.id.eq(orderStatusChangeLog.order.id))
                .where(memberOrder.member.loginId.eq(loginId))
                .groupBy(memberOrder.id)
                .having(orderStatusChangeLog.orderStatusCode.max().eq(code))
                .having(orderStatusChangeLog.orderStatusCode.max().lt(OrderStatusCode.REFUND))
                .fetch().size();

        return PageableExecutionUtils.getPage(data, pageable, () -> totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getOrderCountByStatusCode(String loginId, OrderStatusCode code) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        QOrderStatusChangeLog orderStatusChangeLog = QOrderStatusChangeLog.orderStatusChangeLog;

        return queryFactory.select(memberOrder.id)
                .from(memberOrder)
                .leftJoin(orderStatusChangeLog)
                .on(memberOrder.id.eq(orderStatusChangeLog.order.id))
                .where(memberOrder.member.loginId.eq(loginId))
                .groupBy(memberOrder.id)
                .having(orderStatusChangeLog.orderStatusCode.max().eq(code))
                .having(orderStatusChangeLog.orderStatusCode.max().lt(OrderStatusCode.REFUND))
                .fetch()
                .size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrderSummaryResponseDto> getHiddenOrderByLoginId(
            String loginId,
            Pageable pageable
    ) {
        QMemberOrder memberOrder = QMemberOrder.memberOrder;
        QOrderStatusChangeLog orderStatusChangeLog = QOrderStatusChangeLog.orderStatusChangeLog;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;

        List<OrderSummaryResponseDto> data = queryFactory.select(Projections.constructor(
                        OrderSummaryResponseDto.class,
                        memberOrder.id,
                        memberOrder.orderNumber,
                        memberOrder.orderDateTime,
                        memberOrder.name,
                        memberOrder.totalAmount,
                        ExpressionUtils.as(queryFactory.select(orderStatusChangeLog.orderStatusCode.max())
                                .from(orderStatusChangeLog)
                                .innerJoin(memberOrder)
                                .on(orderStatusChangeLog.order.id.eq(memberOrder.id))
                                .where(memberOrder.member.loginId.eq(loginId)), "orderStatusCode"),
                        memberOrder.member.id,
                        memberOrder.member.name,
                        orderProduct.count(),
                        orderProduct.quantity.sum(),
                        memberOrder.orderCode
                ))
                .from(memberOrder)
                .leftJoin(orderProduct)
                .on(memberOrder.id.eq(orderProduct.order.id))
                .where(memberOrder.member.loginId.eq(loginId).and(memberOrder.isHidden.isTrue()))
                .groupBy(memberOrder.id)
                .orderBy(memberOrder.orderDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(memberOrder.count())
                .from(memberOrder)
                .where(memberOrder.member.loginId.eq(loginId).and(memberOrder.isHidden.isTrue()));

        return PageableExecutionUtils.getPage(data, pageable, countQuery::fetchFirst);
    }
}
