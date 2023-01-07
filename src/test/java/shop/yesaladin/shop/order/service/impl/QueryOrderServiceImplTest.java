package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderInPeriodQueryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;

class QueryOrderServiceImplTest {

    private QueryOrderServiceImpl service;
    private QueryOrderRepository<? extends Order> repository;
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-01T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(QueryOrderRepository.class);
        service = new QueryOrderServiceImpl(repository, clock);
    }

    @Test
    @DisplayName("기간 내에 생성된 모든 데이터 조회에 성공한다")
    void getAllOrderListInPeriodSuccessTest() {
        // given
        OrderInPeriodQueryDto queryDto = Mockito.mock(OrderInPeriodQueryDto.class);
        List<OrderSummaryDto> expectedValue = List.of(Mockito.mock(OrderSummaryDto.class));
        Mockito.when(repository.getCountOfOrdersInPeriod(Mockito.any(), Mockito.any()))
                .thenReturn(1);
        Mockito.when(repository.findAllOrdersInPeriod(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(10),
                        Mockito.eq(1)
                ))
                .thenReturn(expectedValue);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock)).thenReturn(LocalDate.now(clock));
        Mockito.when(queryDto.getSize()).thenReturn(10);
        Mockito.when(queryDto.getPage()).thenReturn(1);

        // when
        List<OrderSummaryDto> actual = service.getAllOrderListInPeriod(queryDto);

        // then
        Assertions.assertThat(actual).isEqualTo(expectedValue);

    }

    @Test
    @DisplayName("미래의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByFutureQueryConditionTest() {
        // given
        OrderInPeriodQueryDto queryDto = Mockito.mock(OrderInPeriodQueryDto.class);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock))
                .thenReturn(LocalDate.now(clock).plusDays(1));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("존재하는 데이터 수보다 큰 오프셋으로 조회를 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByOffsetOutOfBounds() {
        // given
        OrderInPeriodQueryDto queryDto = Mockito.mock(OrderInPeriodQueryDto.class);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock)).thenReturn(LocalDate.now(clock));
        Mockito.when(repository.getCountOfOrdersInPeriod(Mockito.any(), Mockito.any()))
                .thenReturn(1);
        Mockito.when(queryDto.getSize()).thenReturn(10);
        Mockito.when(queryDto.getPage()).thenReturn(2);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto))
                .isInstanceOf(PageOffsetOutOfBoundsException.class);
    }
}