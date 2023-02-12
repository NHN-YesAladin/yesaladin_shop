package shop.yesaladin.shop.order.service.impl;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.repository.CommandOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrderStatusChangeLog;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.order.service.inter.CommandOrderCouponService;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;
import shop.yesaladin.shop.product.dto.SubscribeProductOrderResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

class CommandOrderStatusChangeLogServiceImplTest {
    CommandOrderStatusChangeLogService commandOrderStatusChangeLogService;
    CommandOrderRepository<NonMemberOrder> nonMemberOrderCommandOrderRepository;
    CommandOrderRepository<MemberOrder> memberOrderCommandOrderRepository;
    CommandOrderRepository<Subscribe> subscribeCommandOrderRepository;
    QueryOrderRepository queryOrderRepository;
    CommandOrderStatusChangeLogRepository commandOrderStatusChangeLogRepository;
    Member member;
    MemberAddress memberAddress;
    NonMemberOrder nonMemberOrder;
    MemberOrder memberOrder;
    Subscribe subscribe;
    SubscribeProduct subscribeProduct;


    @BeforeEach
    void setUp() {
        //noinspection unchecked
        nonMemberOrderCommandOrderRepository = (CommandOrderRepository<NonMemberOrder>) Mockito.mock(
                CommandOrderRepository.class);
        //noinspection unchecked
        memberOrderCommandOrderRepository = (CommandOrderRepository<MemberOrder>) Mockito.mock(
                CommandOrderRepository.class);
        //noinspection unchecked
        subscribeCommandOrderRepository = (CommandOrderRepository<Subscribe>) Mockito.mock(
                CommandOrderRepository.class);
        queryOrderRepository = Mockito.mock(QueryOrderRepository.class);

        commandOrderStatusChangeLogRepository = Mockito.mock(CommandOrderStatusChangeLogRepository.class);


        commandOrderStatusChangeLogService = new CommandOrderStatusChangeLogServiceImpl(
                commandOrderStatusChangeLogRepository,
                queryOrderRepository);
        member = DummyMember.memberWithId();
        memberAddress = DummyMemberAddress.addressWithId(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();

        nonMemberOrder = DummyOrder.nonMemberOrderWithId();
        memberOrder = DummyOrder.memberOrderWithId(member, memberAddress);
        subscribe = DummyOrder.subscribeWithId(member, memberAddress, subscribeProduct);

    }


    @Test
    @DisplayName("회원 주문 상태 변경 이력 추가 성공")
    void appendMemberOrderStatusChangeLog() throws Exception {
        // given
        OrderStatusCode code = OrderStatusCode.DEPOSIT;
        OrderStatusChangeLog log = DummyOrderStatusChangeLog.orderStatusChangeLog(
                memberOrder,
                code
        );
        Mockito.when(commandOrderStatusChangeLogRepository.save(any())).thenReturn(log);

        // when
        // then
        assertThatCode(() -> commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                LocalDateTime.now(),
                memberOrder,
                code
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 주문 상태 변경 이력 추가 실패 - 다른 status code 요청")
    void appendMemberOrderStatusChangeLog_notMatchCode_fail() throws Exception {
        // given
        OrderStatusCode wrongCode = OrderStatusCode.COMPLETE;
        OrderStatusChangeLog log = DummyOrderStatusChangeLog.orderStatusChangeLog(
                memberOrder,
                wrongCode
        );
        Mockito.when(commandOrderStatusChangeLogRepository.save(any())).thenReturn(log);

        // when
        // then
        LocalDateTime now = LocalDateTime.now();
        assertThatCode(() -> commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                now,
                memberOrder,
                OrderStatusCode.DEPOSIT
        )).isInstanceOf(ClientException.class).hasMessageContaining("잘못된 주문 상태 변경 요청입니다.");
    }

    @Test
    @DisplayName("비회원 주문 상태 변경 이력 추가 성공")
    void appendNonMemberOrderStatusChangeLog() throws Exception {
        // given
        OrderStatusCode code = OrderStatusCode.DEPOSIT;
        OrderStatusChangeLog log = DummyOrderStatusChangeLog.orderStatusChangeLog(
                nonMemberOrder,
                code
        );
        Mockito.when(commandOrderStatusChangeLogRepository.save(any())).thenReturn(log);

        // when
        // then
        assertThatCode(() -> commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                LocalDateTime.now(),
                nonMemberOrder,
                code
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 구독 주문 상태 변경 이력 추가 성공")
    void appendSubscribeOrderStatusChangeLog() throws Exception {
        // given
        OrderStatusCode code = OrderStatusCode.DEPOSIT;
        OrderStatusChangeLog log = DummyOrderStatusChangeLog.orderStatusChangeLog(
                subscribe,
                code
        );
        Mockito.when(commandOrderStatusChangeLogRepository.save(any())).thenReturn(log);

        // when
        // then
        assertThatCode(() -> commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                LocalDateTime.now(),
                subscribe,
                code
        )).doesNotThrowAnyException();
    }

}
