package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderInPeriodQueryDto;
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

    private final QueryOrderRepository<? extends Order> queryOrderRepository;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getAllOrderListInPeriod(OrderInPeriodQueryDto queryDto) {
        // TODO : 관리자 권한 체크
        checkValidQueryCondition(queryDto);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);
        int size = queryDto.getSize();
        int page = queryDto.getPage();

        checkRequestedOffsetInBounds(startDate, endDate, size, page);
        return queryOrderRepository.findAllOrdersInPeriod(startDate, endDate, size, page);
    }

    private void checkValidQueryCondition(OrderInPeriodQueryDto queryDto) {
        if (queryDto.getEndDateOrDefaultValue(clock).isAfter(LocalDate.now(clock))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.FUTURE);
        }
    }

    private void checkRequestedOffsetInBounds(
            LocalDate startDate, LocalDate endDate, int size, int page
    ) {
        int countOfOrder = queryOrderRepository.getCountOfOrdersInPeriod(startDate, endDate);
        int offset = size * (page - 1);
        if (countOfOrder <= offset) {
            throw new PageOffsetOutOfBoundsException(offset, countOfOrder);
        }
    }
}
