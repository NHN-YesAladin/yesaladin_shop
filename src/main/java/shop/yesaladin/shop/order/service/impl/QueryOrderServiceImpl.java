package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
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
    private final QueryMemberService queryMemberService;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrderListInPeriod(
            PeriodQueryRequestDto queryDto, Pageable pageable
    ) {
        // TODO : 관리자 권한 체크
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, pageable);
        return queryOrderRepository.findAllOrdersInPeriod(startDate, endDate, pageable);
    }

    @Override
    public Page<OrderSummaryDto> getAllOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto, long memberId, Pageable pageable
    ) {
        checkValidMemberId(memberId);
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, pageable);
        return queryOrderRepository.findAllOrdersInPeriodByMemberId(
                startDate,
                endDate,
                memberId,
                pageable
        );
    }

    private void checkRequestedOffsetInBounds(
            LocalDate startDate, LocalDate endDate, Pageable pageable
    ) {
        long countOfOrder = queryOrderRepository.getCountOfOrdersInPeriod(startDate, endDate);
        if (countOfOrder <= pageable.getOffset()) {
            throw new PageOffsetOutOfBoundsException((int) pageable.getOffset(), countOfOrder);
        }
    }

    private void checkValidMemberId(long memberId) {
        queryMemberService.findMemberById(memberId);
    }
}
