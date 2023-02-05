package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.exception.OrderNotFoundException;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;
import shop.yesaladin.shop.product.dto.ProductOrderResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 주문 데이터 조회 서비스의 구현체
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryOrderServiceImpl implements QueryOrderService {

    private final QueryOrderRepository queryOrderRepository;
    private final QueryMemberService queryMemberService;
    private final QueryPointHistoryService queryPointHistoryService;
    private final QueryProductService queryProductService;

    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrderListInPeriod(
            PeriodQueryRequestDto queryDto, Pageable pageable
    ) {
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, null, pageable);
        return queryOrderRepository.findAllOrdersInPeriod(startDate, endDate, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto, long memberId, Pageable pageable
    ) {
        checkValidMemberId(memberId);
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, memberId, pageable);
        return queryOrderRepository.findAllOrdersInPeriodByMemberId(
                startDate,
                endDate,
                memberId,
                pageable
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByNumber(String number) {
        return queryOrderRepository.findByOrderNumber(number)
                .orElseThrow(() -> new OrderNotFoundException(number));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderSheetResponseDto getMemberOrderSheetData(
            OrderSheetRequestDto request,
            String loginId
    ) {
        checkValidLoginId(loginId);

        return getOrderSheetDataForMember(request, loginId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderSheetResponseDto getNonMemberOrderSheetData(OrderSheetRequestDto request) {
        return getOrderSheetDataForNonMember(request);
    }

    private OrderSheetResponseDto getOrderSheetDataForMember(
            OrderSheetRequestDto request,
            String loginId
    ) {
        OrderSheetResponseDto memberOrderSheetData = queryMemberService.getMemberForOrder(loginId);

        long point = queryPointHistoryService.getMemberPoint(loginId);
        List<ProductOrderResponseDto> orderProducts = queryProductService.getByOrderProducts(request.getProductList());

        memberOrderSheetData.setPoint(point);
        memberOrderSheetData.setOrderProducts(orderProducts);

        return memberOrderSheetData;
    }

    private OrderSheetResponseDto getOrderSheetDataForNonMember(OrderSheetRequestDto request) {
        List<ProductOrderResponseDto> orderProducts = queryProductService.getByOrderProducts(request.getProductList());

        return new OrderSheetResponseDto(orderProducts);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto,
            long memberId,
            Pageable pageable
    ) {
        checkValidMemberId(memberId);
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, memberId, pageable);
        return queryOrderRepository.findOrdersInPeriodByMemberId(
                startDate,
                endDate,
                memberId,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPaymentResponseDto getPaymentDtoByMemberOrderId(long orderId) {
        return queryOrderRepository.findPaymentDtoByMemberOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private void checkRequestedOffsetInBounds(
            LocalDate startDate, LocalDate endDate, Long memberId, Pageable pageable
    ) {
        long countOfOrder = 0;

        if (Objects.isNull(memberId)) {
            countOfOrder = queryOrderRepository.getCountOfOrdersInPeriod(startDate, endDate);
        } else {
            countOfOrder = queryOrderRepository.getCountOfOrdersInPeriodByMemberId(
                    startDate,
                    endDate,
                    memberId
            );
        }

        if (countOfOrder < pageable.getOffset()) {
            throw new PageOffsetOutOfBoundsException((int) pageable.getOffset(), countOfOrder);
        }
    }

    private void checkValidLoginId(String loginId) {
        if (!queryMemberService.existsLoginId(loginId)) {
            throw new MemberNotFoundException("");
        }
    }

    private void checkValidMemberId(long memberId) {
        queryMemberService.findMemberById(memberId);
    }
}
