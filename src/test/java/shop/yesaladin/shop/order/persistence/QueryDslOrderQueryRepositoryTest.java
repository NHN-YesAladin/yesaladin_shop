package shop.yesaladin.shop.order.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            memberList.add(member);
        }
        for (int i = 0; i < 10; i++) {
            NonMemberOrder nonMemberOrder = NonMemberOrder.builder()
                    .orderNumber("NM-" + i)
                    .name("non-member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.NON_MEMBER_ORDER)
                    .address("address" + i)
                    .nonMemberName("nonMember" + i)
                    .phoneNumber("0101234567" + i)
                    .build();
            nonMemberOrderList.add(nonMemberOrder);
            entityManager.persist(nonMemberOrder);
        }
        for (int i = 0; i < 30; i++) {
            MemberOrder memberOrder = MemberOrder.builder()
                    .orderNumber("M-" + i)
                    .name("member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .member(memberList.get(i % 5))
                    .memberAddress(memberAddressList.get(i % 5))
                    .build();
            memberOrderList.add(memberOrder);
            entityManager.persist(memberOrder);

            OrderStatusChangeLog orderStatusChangeLog = OrderStatusChangeLog.create(
                    memberOrder,
                    LocalDateTime.now(),
                    OrderStatusCode.ORDER
            );
            entityManager.persist(orderStatusChangeLog);

            OrderStatusChangeLog orderStatusChangeLogComplete = OrderStatusChangeLog.create(
                    memberOrder,
                    LocalDateTime.now(),
                    OrderStatusCode.COMPLETE
            );
            entityManager.persist(orderStatusChangeLogComplete);
        }
        for (int i = 0; i < 10; i++) {
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .ISSN("12345" + i)
                    .build();
            Subscribe subscribe = Subscribe.builder()
                    .member(memberList.get(i % 5))
                    .name("subscribe" + i)
                    .memberAddress(memberAddressList.get(i % 5))
                    .nextRenewalDate(LocalDate.of(2023, i + 1, 1))
                    .subscribeProduct(subscribeProduct)
                    .orderNumber("S-" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                    .build();
            SubscribeOrderList subscribeOrder = SubscribeOrderList.builder()
                    .isTransported(true)
                    .subscribe(subscribe)
                    .memberOrder(memberOrderList.get(i % 5))
                    .build();
            entityManager.persist(subscribeProduct);
            entityManager.persist(subscribe);
            entityManager.persist(subscribeOrder);
            this.subscribeList.add(subscribe);
        }

        entityManager.flush();
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
        Assertions.assertThat(actual.get()).hasSize(4);
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
    void getCountOrdersInPeriodByMemberId() {
        // when
        long actual = queryRepository.getCountOfOrdersInPeriodByMemberId(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                memberList.get(0).getId()
        );

        // then
        Assertions.assertThat(actual).isEqualTo(4);
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
                memberList.get(0).getId(),
                PageRequest.of(0, pageSize)
        );

        // then
        Assertions.assertThat(actual.get()).hasSize(5);
        Assertions.assertThat(actual.getContent()).hasSize(pageSize);
    }

}
