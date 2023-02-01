package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.exception.OrderNotFoundException;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

/**
 * 주문 데이터 조회 서비스의 구현체
 *
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryOrderServiceImpl implements QueryOrderService {

    private final QueryOrderRepository queryOrderRepository;
    private final QueryOrderProductRepository queryOrderProductRepository;
    private final QueryMemberService queryMemberService;
    private final Clock clock;

    /**
     * {@inheritDoc}
     *
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
     *
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
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByNumber(String number) {
        return queryOrderRepository.findByOrderNumber(number)
                .orElseThrow(() -> new OrderNotFoundException(number));
    }

    @Override
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
        Page<OrderSummaryResponseDto> orders = queryOrderRepository.findOrdersInPeriodByMemberId(
                startDate,
                endDate,
                memberId,
                pageable
        );
        for (OrderSummaryResponseDto order : orders) {
            order.setOrderProductCount(queryOrderProductRepository.getCountOfOrderProductByOrderId(
                    order.getOrderId()));
        }
        return orders;
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

        if (countOfOrder <= pageable.getOffset()) {
            throw new PageOffsetOutOfBoundsException((int) pageable.getOffset(), countOfOrder);
        }
    }

    private void checkValidMemberId(long memberId) {
        queryMemberService.findMemberById(memberId);
    }
}
