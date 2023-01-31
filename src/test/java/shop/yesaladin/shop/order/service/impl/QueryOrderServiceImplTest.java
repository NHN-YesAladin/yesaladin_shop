package shop.yesaladin.shop.order.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.MemberOrderRequestDto;
import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.exception.OrderNotFoundException;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.point.dto.PointResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;
import shop.yesaladin.shop.product.dto.OrderProductRequestDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

class QueryOrderServiceImplTest {

    private QueryOrderServiceImpl service;
    private QueryOrderRepository repository;
    private QueryMemberService queryMemberService;
    private QueryPointHistoryService queryPointHistoryService;
    private QueryProductService queryProductService;
    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        queryPointHistoryService = Mockito.mock(QueryPointHistoryService.class);
        queryProductService = Mockito.mock(QueryProductService.class);
        repository = Mockito.mock(QueryOrderRepository.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);
        service = new QueryOrderServiceImpl(
                repository,
                queryMemberService,
                queryPointHistoryService,
                queryProductService,
                clock
        );
    }

    @Test
    @DisplayName("기간 내에 생성된 모든 데이터 조회에 성공한다")
    void getAllOrderListInPeriodSuccessTest() {
        // given
        PeriodQueryRequestDto queryDto = Mockito.mock(PeriodQueryRequestDto.class);
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderSummaryDto> expectedValue = PageableExecutionUtils.getPage(List.of((Mockito.mock(
                OrderSummaryDto.class))), pageable, () -> 1);
        Mockito.when(repository.getCountOfOrdersInPeriod(any(), any()))
                .thenReturn(1L);
        Mockito.when(repository.findAllOrdersInPeriod(any(), any(), any()))
                .thenReturn(expectedValue);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock)).thenReturn(LocalDate.now(clock));

        // when
        Page<OrderSummaryDto> actual = service.getAllOrderListInPeriod(queryDto, pageable);

        // then
        Assertions.assertThat(actual).isEqualTo(expectedValue);

    }

    @Disabled
    @Test
    @DisplayName("특정 회원의 기간 내에 생성된 모든 데이터 조회에 성공한다")
    void getAllOrderListInPeriodByMemberIdSuccessTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock).minusDays(1),
                LocalDate.now(clock)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderSummaryDto> expectedValue = PageableExecutionUtils.getPage(List.of((Mockito.mock(
                OrderSummaryDto.class))), pageable, () -> 1);
        long expectedMemberId = 1L;

        Mockito.when(repository.getCountOfOrdersInPeriod(any(), any()))
                .thenReturn(1L);
        Mockito.when(repository.findAllOrdersInPeriodByMemberId(queryDto.getStartDateOrDefaultValue(
                        clock), queryDto.getEndDateOrDefaultValue(clock), expectedMemberId, pageable))
                .thenReturn(expectedValue);

        // when
        Page<OrderSummaryDto> actual = service.getAllOrderListInPeriodByMemberId(
                queryDto,
                expectedMemberId,
                pageable
        );

        // then
        Assertions.assertThat(actual).isEqualTo(expectedValue);
        Mockito.verify(repository, Mockito.times(1))
                .findAllOrdersInPeriodByMemberId(
                        queryDto.getStartDateOrDefaultValue(clock),
                        queryDto.getEndDateOrDefaultValue(clock),
                        expectedMemberId,
                        pageable
                );
    }

    @Test
    @DisplayName("미래의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByFutureQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock),
                LocalDate.now(clock).plusDays(1)
        );

        Pageable pageable = PageRequest.of(1, 10);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("너무 긴 기간의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByTooLongPeriodQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock).minusMonths(13),
                LocalDate.now(clock)
        );

        Pageable pageable = PageRequest.of(1, 10);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("너무 과거의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByTooPastQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.of(2022, 12, 31),
                LocalDate.now(clock)
        );

        Pageable pageable = PageRequest.of(1, 10);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("시작 날짜가 끝 날짜보다 뒤인 조거능로 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByStartOverEndQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock),
                LocalDate.now(clock).minusDays(1)
        );

        Pageable pageable = PageRequest.of(1, 10);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("존재하는 데이터 수보다 큰 오프셋으로 조회를 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByOffsetOutOfBounds() {
        // given
        PeriodQueryRequestDto queryDto = Mockito.mock(PeriodQueryRequestDto.class);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock)).thenReturn(LocalDate.now(clock));
        Mockito.when(repository.getCountOfOrdersInPeriod(any(), any()))
                .thenReturn(1L);
        Pageable pageable = PageRequest.of(2, 10);

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(PageOffsetOutOfBoundsException.class);
    }

    @Test
    @DisplayName("주문 번호로 주문을 조회에 성공한다.")
    void getOrderByNumber() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        MemberOrder memberOrder = DummyOrder.memberOrder(member, memberAddress);
        Mockito.when(repository.findByOrderNumber(any()))
                .thenReturn(Optional.of(memberOrder));

        // when
        Order order = service.getOrderByNumber(memberOrder.getOrderNumber());

        // then
        Assertions.assertThat(order.getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        Assertions.assertThat(order.getName()).isEqualTo(memberOrder.getName());

        Mockito.verify(repository, Mockito.times(1))
                .findByOrderNumber(stringArgumentCaptor.capture());
        Assertions.assertThat(stringArgumentCaptor.getValue())
                .isEqualTo(memberOrder.getOrderNumber());

    }

    @Test
    @DisplayName("잘못된 주문 번호로 주문을 조회에 실패하여 예외가 발생한다.")
    void getOrderByNumberFailWrongNumber() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        String wrongData = "WrongData";

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getOrderByNumber(wrongData))
                .isInstanceOf(OrderNotFoundException.class);

        Mockito.verify(repository, Mockito.times(1))
                .findByOrderNumber(stringArgumentCaptor.capture());
        Assertions.assertThat(stringArgumentCaptor.getValue())
                .isEqualTo(wrongData);

    }

    @Test
    @Disabled
    @DisplayName("주문서에 필요한 데이터 조회한다.")
    void getMemberOrderSheetData() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";
        String address = "address";
        long amount = 1000;

        List<OrderProductRequestDto> productRequest = new ArrayList<>();
        MemberOrderRequestDto request = new MemberOrderRequestDto(productRequest);
        MemberOrderResponseDto response = new MemberOrderResponseDto(name, phoneNumber, address);

        Mockito.when(queryPointHistoryService.getMemberPoint(loginId)).thenReturn(amount);
        Mockito.when(queryProductService.getProductForOrder(any())).thenReturn(new ArrayList<>());
        Mockito.when(queryMemberService.getMemberForOrder(loginId)).thenReturn(response);

        //when
        MemberOrderResponseDto result = service.getMemberOrderSheetData(request, loginId);

        //then
        Assertions.assertThat(result.getName()).isEqualTo(name);
        Assertions.assertThat(result.getPhoneNumber()).isEqualTo(phoneNumber);
        Assertions.assertThat(result.getAddress()).isEqualTo(address);
        Assertions.assertThat(result.getPoint()).isEqualTo(amount);
        Assertions.assertThat(result.getOrderProducts()).hasSize(0);
    }
}
