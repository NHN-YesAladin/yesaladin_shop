package shop.yesaladin.shop.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.exception.OrderNotFoundException;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

class QueryOrderServiceImplTest {

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    long expectedMemberId = 1L;
    private QueryOrderServiceImpl service;
    private QueryOrderRepository repository;
    private QueryOrderProductRepository queryOrderProductRepository;
    private QueryMemberService queryMemberService;
    private QueryMemberAddressService queryMemberAddressService;
    private QueryPointHistoryService queryPointHistoryService;
    private QueryProductService queryProductService;
    private QueryMemberCouponService queryMemberCouponService;
    private QueryPaymentService queryPaymentService;

    @BeforeEach
    void setUp() {
        queryPointHistoryService = Mockito.mock(QueryPointHistoryService.class);
        queryProductService = Mockito.mock(QueryProductService.class);
        repository = Mockito.mock(QueryOrderRepository.class);
        queryOrderProductRepository = Mockito.mock(QueryOrderProductRepository.class);
        queryMemberAddressService = Mockito.mock(QueryMemberAddressService.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);
        queryMemberCouponService = Mockito.mock(QueryMemberCouponService.class);
        queryPaymentService = Mockito.mock(QueryPaymentService.class);

        service = new QueryOrderServiceImpl(
                repository,
                queryOrderProductRepository,
                queryMemberService,
                queryMemberAddressService,
                queryPointHistoryService,
                queryProductService,
                queryMemberCouponService,
                queryPaymentService,
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
        assertThat(actual).isEqualTo(expectedValue);

    }

    @Disabled("차후 수정")
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
        assertThat(actual).isEqualTo(expectedValue);
        Mockito.verify(repository, Mockito.times(1))
                .findAllOrdersInPeriodByMemberId(
                        queryDto.getStartDateOrDefaultValue(clock),
                        queryDto.getEndDateOrDefaultValue(clock),
                        expectedMemberId,
                        pageable
                );
    }

    @Test
    @DisplayName("특정 회원의 기간 내에 생성된 모든 데이터 조회에 성공한다")
    void getOrderListInPeriodByMemberIdSuccessTest() {
        // given
        LocalDate startDate = LocalDate.now(clock).minusDays(15);
        LocalDate endDate = LocalDate.now(clock);
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                startDate,
                endDate
        );
        Pageable pageable = PageRequest.of(0, 10);

        List<OrderSummaryResponseDto> response = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            response.add(new OrderSummaryResponseDto(
                    (long) i,
                    "orderNumber" + i,
                    LocalDateTime.now().minusDays(5),
                    "name",
                    (long) 10000 * i,
                    OrderStatusCode.ORDER,
                    (long) i,
                    "memberName",
                    (long) i,
                    i,
                    OrderCode.MEMBER_ORDER
            ));
        }

        Page<OrderSummaryResponseDto> expectedValue = PageableExecutionUtils.getPage(
                response,
                pageable,
                () -> 1
        );

        Mockito.when(repository.getCountOfOrdersInPeriodByMemberId(
                startDate,
                endDate,
                expectedMemberId
        )).thenReturn((long) response.size());
        Mockito.when(repository.findOrdersInPeriodByMemberId(queryDto.getStartDateOrDefaultValue(
                        clock), queryDto.getEndDateOrDefaultValue(clock), expectedMemberId, pageable))
                .thenReturn(expectedValue);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        Page<OrderSummaryResponseDto> actual = service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        );

        // then
        assertThat(actual).isEqualTo(expectedValue);
        Mockito.verify(repository, Mockito.times(1))
                .findOrdersInPeriodByMemberId(
                        queryDto.getStartDateOrDefaultValue(clock),
                        queryDto.getEndDateOrDefaultValue(clock),
                        expectedMemberId,
                        pageable
                );
        Mockito.verify(queryMemberService, Mockito.times(1)).findByLoginId(any());
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
        assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
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
        assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("너무 과거의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getAllOrderListInPeriodFailCauseByTooPastQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock).minusYears(1).minusDays(1),
                LocalDate.now(clock)
        );

        Pageable pageable = PageRequest.of(1, 10);

        // when
        // then
        assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
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
        assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
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
        assertThatThrownBy(() -> service.getAllOrderListInPeriod(queryDto, pageable))
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
        assertThat(order.getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(order.getName()).isEqualTo(memberOrder.getName());

        Mockito.verify(repository, Mockito.times(1))
                .findByOrderNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue())
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
        assertThatThrownBy(() -> service.getOrderByNumber(wrongData))
                .isInstanceOf(OrderNotFoundException.class);

        Mockito.verify(repository, Mockito.times(1))
                .findByOrderNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue())
                .isEqualTo(wrongData);

    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 존재하지 않는 회원")
    void getMemberOrderSheetData_fail_member_not_found() {
        //given
        String loginId = "user@1";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(false);

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 존재하지않는 회원")
    void getMemberOrderSheetData_failInGetMemberForOrder_memberNotFound() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenThrow(new ClientException(
                ErrorCode.MEMBER_NOT_FOUND, ""));

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 존재하지않는 회원")
    void getMemberOrderSheetData_failInGetMemberAddressByLoginId_memberNotFound() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenReturn(response);
        Mockito.when(queryMemberAddressService.getByLoginId(loginId))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 구매 불가능한 상품")
    void getMemberOrderSheetData_fail_notAvailableToOrder() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenReturn(response);
        Mockito.when(queryMemberAddressService.getByLoginId(loginId))
                .thenReturn(Lists.newArrayList());
        Mockito.when(queryProductService.getByOrderProducts(any()))
                .thenThrow(new ClientException(ErrorCode.BAD_REQUEST, ""));

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 상품 재고 부족")
    void getMemberOrderSheetData_fail_lackOfProduct() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenReturn(response);
        Mockito.when(queryMemberAddressService.getByLoginId(loginId))
                .thenReturn(Lists.newArrayList());
        Mockito.when(queryProductService.getByOrderProducts(any()))
                .thenThrow(new ClientException(ErrorCode.PRODUCT_NOT_AVAILABLE_TO_ORDER, ""));

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 실패 - 존재하지않는 회원")
    void getMemberOrderSheetData_fail_getMemberPoint_memberNotFound() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenReturn(response);
        Mockito.when(queryMemberAddressService.getByLoginId(loginId))
                .thenReturn(Lists.newArrayList());
        Mockito.when(queryProductService.getByOrderProducts(any())).thenReturn(new ArrayList<>());
        Mockito.when(queryMemberCouponService.getMemberCouponSummaryList(any(), any(), eq(true)))
                .thenReturn(PaginatedResponseDto.<MemberCouponSummaryDto>builder()
                        .dataList(Lists.newArrayList()).build());
        Mockito.when(queryPointHistoryService.getMemberPoint(any()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when, then
        assertThatThrownBy(() ->
                service.getMemberOrderSheetData(request, loginId)
        ).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문서에 필요한 데이터 조회 성공")
    void getMemberOrderSheetData() {
        //given
        String loginId = "user@1";
        String name = "test";
        String phoneNumber = "01012341234";
        String address = "address";
        long amount = 1000;

        List<String> isbn = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbn, quantity);
        MemberOrderSheetResponseDto response = new MemberOrderSheetResponseDto(
                name,
                phoneNumber,
                7
        );

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.getMemberForOrder(any())).thenReturn(response);
        Mockito.when(queryMemberAddressService.getByLoginId(loginId))
                .thenReturn(Lists.newArrayList());
        Mockito.when(queryProductService.getByOrderProducts(any())).thenReturn(new ArrayList<>());
        Mockito.when(queryMemberCouponService.getMemberCouponSummaryList(any(), any(), eq(true)))
                .thenReturn(PaginatedResponseDto.<MemberCouponSummaryDto>builder()
                        .dataList(Lists.newArrayList()).build());
        Mockito.when(queryPointHistoryService.getMemberPoint(any())).thenReturn(amount);

        //when
        OrderSheetResponseDto result = service.getMemberOrderSheetData(request, loginId);

        //then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.getPoint()).isEqualTo(amount);
        assertThat(result.getOrderProducts()).hasSize(0);
    }

    @Test
    @DisplayName("미래의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getOrderListInPeriodFailCauseByFutureQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock),
                LocalDate.now(clock).plusDays(1)
        );

        Pageable pageable = PageRequest.of(1, 10);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        // then
        assertThatThrownBy(() -> service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        ))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("너무 긴 기간의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getOrderListInPeriodFailCauseByTooLongPeriodQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock).minusMonths(13),
                LocalDate.now(clock)
        );

        Pageable pageable = PageRequest.of(1, 10);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        // then
        assertThatThrownBy(() -> service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        ))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("너무 과거의 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getOrderListInPeriodFailCauseByTooPastQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock).minusYears(1).minusDays(1),
                LocalDate.now(clock)
        );

        Pageable pageable = PageRequest.of(1, 10);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        // then
        assertThatThrownBy(() -> service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        ))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("시작 날짜가 끝 날짜보다 뒤인 조건으로 데이터를 조회하려고 시도하면 예외가 발생한다")
    void getOrderListInPeriodFailCauseByStartOverEndQueryConditionTest() {
        // given
        PeriodQueryRequestDto queryDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                LocalDate.now(clock),
                LocalDate.now(clock).minusDays(1)
        );

        Pageable pageable = PageRequest.of(1, 10);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        // then
        assertThatThrownBy(() -> service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        ))
                .isInstanceOf(InvalidPeriodConditionException.class);
    }

    @Test
    @DisplayName("존재하는 데이터 수보다 큰 오프셋으로 조회를 시도하면 예외가 발생한다")
    void getOrderListInPeriodFailCauseByOffsetOutOfBounds() {
        // given
        PeriodQueryRequestDto queryDto = Mockito.mock(PeriodQueryRequestDto.class);
        Mockito.when(queryDto.getEndDateOrDefaultValue(clock)).thenReturn(LocalDate.now(clock));
        Mockito.when(repository.getCountOfOrdersInPeriod(any(), any()))
                .thenReturn(1L);
        Pageable pageable = PageRequest.of(2, 10);

        Member member = DummyMember.memberWithId();
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);

        // when
        // then
        assertThatThrownBy(() -> service.getOrderListInPeriodByMemberId(
                queryDto,
                member.getLoginId(),
                pageable
        ))
                .isInstanceOf(PageOffsetOutOfBoundsException.class);
    }

    @Test
    @DisplayName("주문 상태에 따른 주문 조회 성공")
    void getStatusResponsesByLoginIdAndStatus() throws Exception {
        // given
        Member member = DummyMember.memberWithId();

        List<OrderStatusResponseDto> responseList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderStatusResponseDto responseDto = OrderStatusResponseDto.builder()
                    .orderId((long) i)
                    .totalAmount((long) (10000 * i))
                    .orderName("orderName" + i)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .orderNumber("number" + i)
                    .receiverName("김김김" + i)
                    .orderDateTime(LocalDateTime.now().plusHours(i))
                    .loginId("loginId" + i)
                    .build();
            responseList.add(responseDto);
        }
        PageRequest pageRequest = PageRequest.of(1, 3);
        Mockito.when(repository.findSuccessStatusResponsesByLoginIdAndStatus(any(), any(), any()))
                .thenReturn(new PageImpl<>(responseList, pageRequest, responseList.size()));

        Mockito.when(queryMemberService.existsLoginId(any())).thenReturn(true);

        // when
        Page<OrderStatusResponseDto> responses = service.getStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.ORDER,
                pageRequest
        );

        // then
        assertThat(responses).hasSize(10);
        assertThat(responses.getContent().get(0).getOrderId())
                .isEqualTo(responseList.get(0).getOrderId());
        assertThat(responses.getContent().get(0).getOrderName())
                .isEqualTo(responseList.get(0).getOrderName());
        assertThat(responses.getContent().get(1).getOrderId())
                .isEqualTo(responseList.get(1).getOrderId());
        assertThat(responses.getNumber()).isEqualTo(pageRequest.getPageNumber());

        Mockito.verify(repository, Mockito.times(1))
                .findSuccessStatusResponsesByLoginIdAndStatus(any(), any(), any());
        Mockito.verify(queryMemberService, Mockito.times(1)).existsLoginId(any());
    }

    @Test
    @DisplayName("주문 상태에 따른 주문 조회 실패 - 없는 회원 아이디")
    void getStatusResponsesByLoginIdAndStatus_notExistMember() throws Exception {
        // given
        Member member = DummyMember.memberWithId();

        List<OrderStatusResponseDto> responseList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderStatusResponseDto responseDto = OrderStatusResponseDto.builder()
                    .orderId((long) i)
                    .totalAmount((long) (10000 * i))
                    .orderName("orderName" + i)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .orderNumber("number" + i)
                    .receiverName("김김김" + i)
                    .orderDateTime(LocalDateTime.now().plusHours(i))
                    .loginId("loginId" + i)
                    .build();
            responseList.add(responseDto);
        }
        PageRequest pageRequest = PageRequest.of(0, 5);
        Mockito.when(repository.findSuccessStatusResponsesByLoginIdAndStatus(any(), any(), any()))
                .thenReturn(new PageImpl<>(responseList, pageRequest, responseList.size()));

        Mockito.when(queryMemberService.existsLoginId(any())).thenReturn(false);

        // when
        // then
        assertThatCode(() -> service.getStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.ORDER,
                pageRequest
        ))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member not found with loginId");

        Mockito.verify(repository, Mockito.never())
                .findSuccessStatusResponsesByLoginIdAndStatus(any(), any(), any());
        Mockito.verify(queryMemberService, Mockito.times(1)).existsLoginId(any());
    }

    @Test
    @DisplayName("주문 상태에 맞는 주문 개수를 조회 성공")
    void getOrderCountByLoginIdStatus() throws Exception {
        // given
        Member member = DummyMember.memberWithId();

        Mockito.when(repository.getOrderCountByStatusCode(any(), any())).thenReturn(3L);
        Mockito.when(queryMemberService.existsLoginId(any())).thenReturn(true);

        // when
        Map<OrderStatusCode, Long> statusCodeLongMap = service.getOrderCountByLoginIdStatus(
                member.getLoginId());

        // then
        Assertions.assertThat(statusCodeLongMap).hasSize(4);
        Assertions.assertThat(statusCodeLongMap).containsEntry(OrderStatusCode.ORDER, 3L);
        Assertions.assertThat(statusCodeLongMap).doesNotContainEntry(OrderStatusCode.DEPOSIT, 3L);

        Mockito.verify(repository, Mockito.times(4)).getOrderCountByStatusCode(any(), any());
        Mockito.verify(queryMemberService, Mockito.times(1)).existsLoginId(any());
    }

    @Test
    @DisplayName("주문 상태에 맞는 주문 개수를 조회 실패")
    void getOrderCountByLoginIdStatus_notExistMember() throws Exception {
        // given
        Member member = DummyMember.memberWithId();

        Mockito.when(repository.getOrderCountByStatusCode(any(), any())).thenReturn(3L);
        Mockito.when(queryMemberService.existsLoginId(any())).thenReturn(false);

        // when

        // then
        Assertions.assertThatCode(() -> service.getOrderCountByLoginIdStatus(
                        member.getLoginId()))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member not found with loginId");

        Mockito.verify(repository, Mockito.never()).getOrderCountByStatusCode(any(), any());
        Mockito.verify(queryMemberService, Mockito.times(1)).existsLoginId(any());
    }
}
