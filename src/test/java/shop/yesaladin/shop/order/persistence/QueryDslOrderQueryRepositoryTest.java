package shop.yesaladin.shop.order.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;


@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslOrderQueryRepositoryTest {

    @Autowired
    private QueryDslOrderQueryRepository queryRepository;
    @Autowired
    private EntityManager entityManager;
    private List<NonMemberOrder> nonMemberOrderList;
    private List<MemberOrder> memberOrderList;
    private List<Subscribe> subscribeList;
    private List<Member> memberList;
    private List<MemberAddress> memberAddressList;
    private List<OrderStatusChangeLog> logList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        nonMemberOrderList = new ArrayList<>();
        memberOrderList = new ArrayList<>();
        subscribeList = new ArrayList<>();
        memberList = new ArrayList<>();
        memberAddressList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                    .nickname("test" + i)
                    .name("name" + i)
                    .loginId("id" + i)
                    .password("password")
                    .birthYear(2002)
                    .birthMonth(5)
                    .birthDay(i)
                    .email("admin+" + i + "@mongmeo.dev")
                    .phone("0101234567" + i)
                    .signUpDate(LocalDate.of(2023, 1, i + 1))
                    .withdrawalDate(null)
                    .isWithdrawal(false)
                    .isBlocked(false)
                    .memberGrade(MemberGrade.WHITE)
                    .memberGenderCode(MemberGenderCode.MALE)
                    .build();

            MemberAddress memberAddress = MemberAddress.builder()
                    .member(member)
                    .address("address" + i)
                    .build();

            memberList.add(member);
            memberAddressList.add(memberAddress);

            entityManager.persist(member);
            entityManager.persist(memberAddress);
        }
        for (int i = 0; i < 10; i++) {
            NonMemberOrder nonMemberOrder = NonMemberOrder.builder()
                    .orderNumber("NonMember-order-" + i)
                    .name("non-member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(3000)
                    .wrappingFee(3000)
                    .totalAmount(16000)
                    .orderCode(OrderCode.NON_MEMBER_ORDER)
                    .address("address" + i)
                    .nonMemberName("nonMember" + i)
                    .phoneNumber("0101234567" + i)
                    .recipientName("수령인 이름")
                    .recipientPhoneNumber("수령인 폰번호")
                    .build();
            nonMemberOrderList.add(nonMemberOrder);
            entityManager.persist(nonMemberOrder);
        }
        for (int i = 0; i < 30; i++) {
            int index = i % 5;
            Member member = memberList.get(index);
            MemberOrder memberOrder = MemberOrder.builder()
                    .orderNumber("Member-order-" + i)
                    .name("member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(3000)
                    .wrappingFee(3000)
                    .totalAmount(16000)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .recipientName("friend")
                    .recipientPhoneNumber("01011111111")
                    .member(member)
                    .memberAddress(memberAddressList.get(index))
                    .recipientName("수령인 이름")
                    .recipientPhoneNumber("수령인 폰번호")
                    .build();
            memberOrderList.add(memberOrder);
            entityManager.persist(memberOrder);

            persistStatusLog(index, memberOrder);
        }
        for (int i = 0; i < 10; i++) {
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .ISSN("12345" + i)
                    .build();
            int index = i % 5;
            Subscribe subscribe = Subscribe.builder()
                    .member(memberList.get(index))
                    .name("subscribe" + i)
                    .memberAddress(memberAddressList.get(index))
                    .nextRenewalDate(LocalDate.of(2023, i + 1, 1))
                    .subscribeProduct(subscribeProduct)
                    .orderNumber("Subscribe-order-" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(3000)
                    .wrappingFee(3000)
                    .totalAmount(16000)
                    .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                    .recipientName("수령인 이름")
                    .recipientPhoneNumber("수령인 폰번호")
                    .build();
            SubscribeOrderList subscribeOrder = SubscribeOrderList.builder()
                    .isTransported(true)
                    .subscribe(subscribe)
                    .memberOrder(memberOrderList.get(index))
                    .build();
            entityManager.persist(subscribeProduct);
            entityManager.persist(subscribe);
            entityManager.persist(subscribeOrder);
            this.subscribeList.add(subscribe);
            persistStatusLog(index, subscribe);
        }

        entityManager.flush();
    }

    private void persistStatusLog(int index, MemberOrder memberOrder) {
        OrderStatusChangeLog orderStatusChangeLogOrder = OrderStatusChangeLog.create(
                memberOrder,
                LocalDateTime.now(),
                OrderStatusCode.ORDER
        );
        entityManager.persist(orderStatusChangeLogOrder);

        if (index == 0) {
            return;
        }
        OrderStatusChangeLog orderStatusChangeLogDeposit = OrderStatusChangeLog.create(
                memberOrder,
                LocalDateTime.now().plusSeconds(5),
                OrderStatusCode.DEPOSIT
        );
        entityManager.persist(orderStatusChangeLogDeposit);

        if (index == 1) {
            return;
        }
        OrderStatusChangeLog orderStatusChangeLogReady = OrderStatusChangeLog.create(
                memberOrder,
                LocalDateTime.now().plusMinutes(5),
                OrderStatusCode.READY
        );
        entityManager.persist(orderStatusChangeLogReady);
        logList.add(orderStatusChangeLogReady);
        if (index == 2) {
            return;
        }
        OrderStatusChangeLog orderStatusChangeLogDelivery = OrderStatusChangeLog.create(
                memberOrder,
                LocalDateTime.now().plusHours(1),
                OrderStatusCode.DELIVERY
        );
        entityManager.persist(orderStatusChangeLogDelivery);

        if (index == 3) {
            return;
        }
        OrderStatusChangeLog orderStatusChangeLogComplete = OrderStatusChangeLog.create(
                memberOrder,
                LocalDateTime.now().plusHours(2),
                OrderStatusCode.COMPLETE
        );
        entityManager.persist(orderStatusChangeLogComplete);

    }

    @Test
    @DisplayName("비회원 주문 조회에 성공한다.")
    void findByIdNonMemberOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(nonMemberOrderList.get(0).getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(nonMemberOrderList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.NON_MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("회원 주문 조회에 성공한다.")
    void findByIdMemberOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(memberOrderList.get(0).getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(memberOrderList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get()).isInstanceOf(OrderCode.MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("구독 주문 조회에 성공한다.")
    void findByIdSubscribeOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(subscribeList.get(0).getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(subscribeList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.MEMBER_SUBSCRIBE.getOrderClass());
    }

    @Test
    @DisplayName("특정 기간 내 주문 기록 조회에 성공한다.")
    void findAllOrdersInPeriod() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriod(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 2), PageRequest.of(0, 10));

        // then
        Assertions.assertThat(actual.get()).hasSize(6);
    }

    @Test
    @DisplayName("특정 기간 내 주문 기록이 페이지네이션 되어 조회된다.")
    void findAllOrdersInPeriodWithPagination() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriod(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 4), PageRequest.of(0, 10));

        // then
        Assertions.assertThat(actual.get()).hasSize(10);
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 기록 조회에 성공한다.")
    void findAllOrdersInPeriodByMemberId() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriodByMemberId(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5),
                memberList.get(0).getId(),
                PageRequest.of(0, 20)
        );

        // then
        Assertions.assertThat(actual.get()).hasSize(2);
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 기록이 페이지네이션 되어 조회된다.")
    void findAllOrdersInPeriodByMemberIdWithPagination() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriodByMemberId(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 31),
                memberList.get(0).getId(),
                PageRequest.of(0, 5)
        );

        // then
        Assertions.assertThat(actual.get()).hasSize(5);
    }

    @Test
    @DisplayName("특정 기간 내 주문 수가 반환된다.")
    void getCountOrdersInPeriod() {
        // when
        long actual = queryRepository.getCountOfOrdersInPeriod(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 4)
        );

        // then
        Assertions.assertThat(actual).isEqualTo(12);
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 수가 반환된다.")
    void getCountOrdersInPeriodById() {
        // when
        long actual = queryRepository.getCountOfOrdersInPeriodByMemberId(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                memberList.get(0).getId()
        );

        // then
        Assertions.assertThat(actual).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 번호로 비회원 주문 조회에 성공한다.")
    void findByOrderNumberNonMemberOrderTest() throws Exception {
        // when
        Optional<Order> actual = queryRepository.findByOrderNumber(nonMemberOrderList.get(0)
                .getOrderNumber());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(nonMemberOrderList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.NON_MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("주문번호로 회원 주문 조회에 성공한다.")
    void findByOrderNumberMemberOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findByOrderNumber(memberOrderList.get(0)
                .getOrderNumber());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(memberOrderList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get()).isInstanceOf(OrderCode.MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("주문번호로 구독 주문 조회에 성공한다.")
    void findByOrderNumberSubscribeOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findByOrderNumber(subscribeList.get(0)
                .getOrderNumber());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(subscribeList.get(0).getOrderNumber());
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.MEMBER_SUBSCRIBE.getOrderClass());
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 수가 반환된다. - 전체 주문 조회용")
    void findOrdersInPeriodByMemberId() throws Exception {
        // given
        String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
        String ISBN = "0000000000001";
        int size = 100;

        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();
        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        MemberOrder memberOrder = memberOrderList.get(0);
        for (int i = 0; i < size; i++) {
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            entityManager.persist(product);

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(memberOrder)
                    .product(product)
                    .isCanceled(false)
                    .quantity(10)
                    .build();
            entityManager.persist(orderProduct);
        }

        // when
        int pageSize = 5;
        Page<OrderSummaryResponseDto> actual = queryRepository.findOrdersInPeriodByMemberId(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 31),
                memberList.get(1).getId(),
                PageRequest.of(0, pageSize)
        );

        // then
        Assertions.assertThat(actual.get()).hasSize(5);
        Assertions.assertThat(actual.getContent()).hasSize(pageSize);
        Assertions.assertThat(actual.getContent().get(0).getOrderStatusCode())
                .isEqualTo(OrderStatusCode.DEPOSIT);
    }

    @Test
    @DisplayName("회원 주문 번호를 통해 결제 이후의 제공할 정보를 조회한다.")
    void findPaymentDtoByOrderId() throws Exception {
        // given
        MemberOrder memberOrder = memberOrderList.get(0);

        // when
        Optional<OrderPaymentResponseDto> responseDto = queryRepository.findPaymentDtoByMemberOrderId(
                memberOrder.getId());

        // then
        Assertions.assertThat(responseDto.get().getOrdererName())
                .isEqualTo(memberOrder.getMember().getName());
        Assertions.assertThat(responseDto.get().getAddress())
                .isEqualTo(memberOrder.getMemberAddress().getAddress());
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("주문 상태 조회 성공 ")
    void findStatusResponsesByLoginIdAndStatusCode_ready(int index) throws Exception {
        // given
        Member member = memberList.get(index);

        // when
        OrderStatusCode code = Arrays.stream(OrderStatusCode.values())
                .filter(c -> c.getStatusCode() == (index + 1))
                .findFirst()
                .get();

        Page<OrderStatusResponseDto> responses = queryRepository.findOrderStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                code,
                PageRequest.of(2, 2)
        );

        // then
        Assertions.assertThat(responses).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("주문 상태 조회 실패 - ORDER 일 때, 맞지않는 코드 값 ")
    void findStatusResponsesByLoginIdAndStatusCode_notMatchIncrease(int index) throws Exception {
        // given
        Member member = memberList.get(index);

        // when
        Page<OrderStatusResponseDto> responses = queryRepository.findOrderStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.ORDER,
                PageRequest.of(0, 300)
        );

        // then
        Assertions.assertThat(responses).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    @DisplayName("주문 상태 조회 실패 - COMPLETE 일 때, 맞지않는 코드 값 ")
    void findStatusResponsesByLoginIdAndStatusCode_notMatchDecrease(int index) throws Exception {
        // given
        Member member = memberList.get(index);

        // when
        Page<OrderStatusResponseDto> responses = queryRepository.findOrderStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.COMPLETE,
                PageRequest.of(0, 300)
        );

        // then
        Assertions.assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("주문 상태 조회 데이터 없음 (실패아님) - CANCEL이 제외이므로")
    void findOrderStatusResponsesByLoginIdAndStatus_CANCEL() throws Exception {
        // given
        Member member = memberList.get(4);
        for (MemberOrder memberOrder : memberOrderList) {
            OrderStatusChangeLog log = OrderStatusChangeLog.create(
                    memberOrder,
                    LocalDateTime.now(),
                    OrderStatusCode.REFUND
            );
            entityManager.persist(log);
        }

        // when
        Page<OrderStatusResponseDto> responses = queryRepository.findOrderStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.COMPLETE,
                PageRequest.of(0, 300)
        );

        // then
        Assertions.assertThat(responses).hasSize(2); //구독 주문만 존재
    }

    @Test
    @DisplayName("주문 상태 조회 데이터 없음 (실패아님) - REFUND이 제외이므로")
    void findOrderStatusResponsesByLoginIdAndStatus_REFUND() throws Exception {
        // given
        Member member = memberList.get(0);
        for (MemberOrder memberOrder : memberOrderList) {
            OrderStatusChangeLog log = OrderStatusChangeLog.create(
                    memberOrder,
                    LocalDateTime.now(),
                    OrderStatusCode.CANCEL
            );
            entityManager.persist(log);
        }

        // when
        Page<OrderStatusResponseDto> responses = queryRepository.findOrderStatusResponsesByLoginIdAndStatus(
                member.getLoginId(),
                OrderStatusCode.ORDER,
                PageRequest.of(0, 300)
        );

        // then
        Assertions.assertThat(responses).hasSize(2); //구독 주문만 존재
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("주문 상태에 해당 하는 주문 개수 조회")
    void getOrderCountByStatusCode(int index) throws Exception {
        // given
        Member member = memberList.get(index);

        // when
        OrderStatusCode code = Arrays.stream(OrderStatusCode.values())
                .filter(c -> c.getStatusCode() == (index + 1))
                .findFirst()
                .get();

        long orderCountByStatusCode = queryRepository.getOrderCountByStatusCode(
                member.getLoginId(),
                code
        );

        // then
        Assertions.assertThat(orderCountByStatusCode).isEqualTo(8); //구독 주문 포함
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    @DisplayName("주문 상태에 해당 하는 주문 개수 조회 실패 - COMPLETE 일 때, 맞지않는 코드 값 ")
    void getOrderCountByStatusCode_notMatchDecrease(int index) throws Exception {
        // given
        Member member = memberList.get(index);

        // when
        long orderCountByStatusCode = queryRepository.getOrderCountByStatusCode(
                member.getLoginId(),
                OrderStatusCode.COMPLETE
        );

        // then
        Assertions.assertThat(orderCountByStatusCode).isZero();
    }
}
