package shop.yesaladin.shop.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.order.domain.model.*;
import shop.yesaladin.shop.order.domain.repository.CommandOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.NonMemberRequestDto;
import shop.yesaladin.shop.order.dto.OrderStatusChangeLogResponseDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 주문 상태 변경 내역 생성과 관련한 service 구현체 입니다.
 *
 * @author 최예린
 * @author 배수한
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandOrderStatusChangeLogServiceImpl implements CommandOrderStatusChangeLogService {

    private final CommandOrderStatusChangeLogRepository commandOrderStatusChangeLogRepository;
    private final QueryOrderRepository queryOrderRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderStatusChangeLogResponseDto createMemberOrderStatusChangeLog(
            long orderId,
            String loginId,
            OrderStatusCode orderStatus
    ) {
        MemberOrder memberOrder = tryGetMemberOrderById(orderId);

        checkUserIsOwnerOfOrderForMember(orderId, loginId, memberOrder);

        OrderStatusChangeLog savedOrderStatusChangeLog = saveOrderStatusChangeLog(
                orderStatus,
                memberOrder
        );
        return OrderStatusChangeLogResponseDto.fromEntity(savedOrderStatusChangeLog);
    }

    private MemberOrder tryGetMemberOrderById(long orderId) {
        return (MemberOrder) queryOrderRepository.findById(orderId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id : " + orderId
                ));
    }

    private void checkUserIsOwnerOfOrderForMember(
            long orderId,
            String loginId,
            MemberOrder memberOrder
    ) {
        if (!isMemberOrder(loginId, memberOrder)) {
            throw new ClientException(
                    ErrorCode.ORDER_BAD_REQUEST,
                    loginId + " is not a owner of order(" + orderId + ")."
            );
        }
    }

    private boolean isMemberOrder(String loginId, MemberOrder memberOrder) {
        return Objects.equals(memberOrder.getMember().getLoginId(), loginId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderStatusChangeLogResponseDto createNonMemberOrderStatusChangeLog(
            Long orderId,
            NonMemberRequestDto request,
            OrderStatusCode orderStatus
    ) {
        NonMemberOrder nonMemberOrder = getNonMemberOrder(orderId);

        checkUserIsOwnerOfOrderForNonMember(orderId, request, nonMemberOrder);

        OrderStatusChangeLog savedOrderStatusChangeLog = saveOrderStatusChangeLog(
                orderStatus,
                nonMemberOrder
        );

        return OrderStatusChangeLogResponseDto.fromEntity(savedOrderStatusChangeLog);
    }

    private NonMemberOrder getNonMemberOrder(Long orderId) {
        return (NonMemberOrder) queryOrderRepository.findById(orderId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id : " + orderId
                ));
    }

    private void checkUserIsOwnerOfOrderForNonMember(
            long orderId,
            NonMemberRequestDto request,
            NonMemberOrder nonMemberOrder
    ) {
        if (!isNonMemberOrder(request, nonMemberOrder)) {
            throw new ClientException(
                    ErrorCode.ORDER_BAD_REQUEST,
                    request.getName() + " is not a owner of order(" + orderId + ")."
            );
        }
    }

    private boolean isNonMemberOrder(NonMemberRequestDto request, NonMemberOrder nonMemberOrder) {
        return Objects.equals(nonMemberOrder.getPhoneNumber(), request.getPhoneNumber()) &&
                Objects.equals(nonMemberOrder.getNonMemberName(), request.getName());
    }

    private OrderStatusChangeLog saveOrderStatusChangeLog(
            OrderStatusCode orderStatus,
            Order order
    ) {
        OrderStatusChangeLog orderStatusChangeLog = OrderStatusChangeLog.create(
                order,
                LocalDateTime.now(),
                orderStatus
        );
        return commandOrderStatusChangeLogRepository.save(orderStatusChangeLog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void appendOrderStatusChangeLog(
            LocalDateTime orderChangeDateTime,
            Order order,
            OrderStatusCode code
    ) {
        appendLog(orderChangeDateTime, order, code);
    }

    private void appendLog(LocalDateTime orderChangeDateTime, Order order, OrderStatusCode code) {
        OrderStatusChangeLog orderStatusChangeLog = OrderStatusChangeLog.create(
                order,
                orderChangeDateTime,
                code
        );
        OrderStatusChangeLog changeLog = commandOrderStatusChangeLogRepository.save(
                orderStatusChangeLog);
        if (!changeLog.getOrderStatusCode().equals(code)) {
            throw new ClientException(ErrorCode.ORDER_BAD_REQUEST, "잘못된 주문 상태 변경 요청입니다.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void appendOrderStatusChangeLogByOrderId(
            LocalDateTime orderChangeDateTime,
            Long orderId,
            OrderStatusCode code
    ) {
        Order order = queryOrderRepository.findById(orderId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id : " + orderId
                ));
        appendLog(orderChangeDateTime, order, code);
    }
}
