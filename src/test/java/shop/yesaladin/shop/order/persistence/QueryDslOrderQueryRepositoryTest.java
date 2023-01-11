package shop.yesaladin.shop.order.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@Transactional
@SpringBootTest
class QueryDslOrderQueryRepositoryTest {

    @Autowired
    private QueryDslOrderQueryRepository queryRepository;
    @Autowired
    private EntityManager entityManager;
    private List<NonMemberOrder> nonMemberOrderList;
    private List<MemberOrder> memberOrderList;
    private List<SubscribeOrderList> subscribeOrderList;
    private List<Member> memberList;
    private List<MemberAddress> memberAddressList;


    @BeforeEach
    void setUp() {
        nonMemberOrderList = new ArrayList<>();
        memberOrderList = new ArrayList<>();
        subscribeOrderList = new ArrayList<>();
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
                    .point(i)
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
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.NON_MEMBER_ORDER)
                    .address("address" + i)
                    .name("nonMember" + i)
                    .phoneNumber("0101234567" + i)
                    .build();
            nonMemberOrderList.add(nonMemberOrder);
            entityManager.persist(nonMemberOrder);
        }
        for (int i = 0; i < 30; i++) {
            MemberOrder memberOrder = MemberOrder.builder()
                    .orderNumber("M-" + i)
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
        }
        for (int i = 0; i < 10; i++) {
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .ISSN("12345" + i)
                    .build();
            Subscribe subscribe = Subscribe.builder()
                    .memberAddress(memberAddressList.get(i % 5))
                    .member(memberList.get(i % 5))
                    .subscribeProduct(subscribeProduct)
                    .intervalMonth(2)
                    .nextRenewalDate(LocalDate.of(2023, i + 1, i + 1))
                    .build();
            SubscribeOrderList subscribeOrderList = SubscribeOrderList.builder()
                    .orderNumber("S-" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedDate(LocalDate.of(2023, 1, i + 2))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                    .isTransported(false)
                    .subscribe(subscribe)
                    .build();
            entityManager.persist(subscribeProduct);
            entityManager.persist(subscribe);
            entityManager.persist(subscribeOrderList);
            this.subscribeOrderList.add(subscribeOrderList);
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
        Optional<Order> actual = queryRepository.findById(subscribeOrderList.get(0).getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(subscribeOrderList.get(0).getOrderNumber());
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

    @Disabled("DB구조 개선 후 수정 예정")
    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 기록 조회에 성공한다.")
    void findAllOrdersInPeriodByMemberId() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriodByMemberId(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 5), memberList.get(0).getId(), PageRequest.of(0, 20));

        // then
        Assertions.assertThat(actual.get()).hasSize(4);
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 내 주문 기록이 페이지네이션 되어 조회된다.")
    void findAllOrdersInPeriodByMemberIdWithPagination() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriodByMemberId(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 31), memberList.get(0).getId(), PageRequest.of(0, 5));

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


}