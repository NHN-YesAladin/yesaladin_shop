package shop.yesaladin.shop.order.persistence;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.domain.dummy.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrder;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@Transactional
@SpringBootTest
class QueryDslOrderQueryRepositoryTest {

    @Autowired
    private QueryDslOrderQueryRepository queryRepository;
    @Autowired
    private EntityManager entityManager;
    private NonMemberOrder nonMemberOrder;
    private MemberOrder memberOrder;
    private SubscribeOrder subscribeOrder;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        nonMemberOrder = DummyOrder.nonMemberOrder();
        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        SubscribeProduct subscribeProduct = DummySubscribeProduct.subscribeProduct();
        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        Subscribe subscribe = DummySubscribe.subscribe(memberAddress, member, subscribeProduct);
        subscribeOrder = DummyOrder.subscribeOrder(subscribe);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(nonMemberOrder);
        entityManager.persist(memberOrder);
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);
        entityManager.persist(subscribeOrder);

        for (int i = 1; i <= 30; i++) {
            NonMemberOrder nonO = DummyOrder.nonMemberOrder();
            MemberOrder memberO = DummyOrder.memberOrder(member, memberAddress);
            subscribe = DummySubscribe.subscribe(memberAddress, member, subscribeProduct);
            SubscribeOrder subO = DummyOrder.subscribeOrder(subscribe);

            LocalDateTime orderDateTime = LocalDateTime.of(2023, 1, i, 0, 0);
            Field orderDateTimeField = Order.class.getDeclaredField("orderDateTime");
            Field orderNumberField = Order.class.getDeclaredField("orderNumber");
            orderDateTimeField.setAccessible(true);
            orderNumberField.setAccessible(true);

            orderDateTimeField.set(nonO, orderDateTime);
            orderDateTimeField.set(memberO, orderDateTime);
            orderDateTimeField.set(subO, orderDateTime);
            orderNumberField.set(nonO, i + "n");
            orderNumberField.set(memberO, i + "m");
            orderNumberField.set(subO, i + "s");

            entityManager.persist(memberAddress);
            entityManager.persist(nonO);
            entityManager.persist(memberO);
            entityManager.persist(subscribe);
            entityManager.persist(subO);
        }

        entityManager.flush();
    }

    @Test
    @DisplayName("비회원 주문 조회에 성공한다.")
    void findByIdNonMemberOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(nonMemberOrder.getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(nonMemberOrder.getOrderNumber());
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.NON_MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("회원 주문 조회에 성공한다.")
    void findByIdMemberOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(memberOrder.getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(memberOrder.getOrderNumber());
        Assertions.assertThat(actual.get()).isInstanceOf(OrderCode.MEMBER_ORDER.getOrderClass());
    }

    @Test
    @DisplayName("구독 주문 조회에 성공한다.")
    void findByIdSubscribeOrderTest() {
        // when
        Optional<Order> actual = queryRepository.findById(subscribeOrder.getId());

        // then
        Assertions.assertThat(actual).isPresent();
        Assertions.assertThat(actual.get().getOrderNumber())
                .isEqualTo(subscribeOrder.getOrderNumber());
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
        Assertions.assertThat(actual.get()).hasSize(9);
    }

    @Test
    @DisplayName("특정 기간 내 주문 기록이 페이지네이션 되어 조회된다.")
    void findAllOrdersInPeriodWithPagination() {
        // when
        Page<OrderSummaryDto> actual = queryRepository.findAllOrdersInPeriod(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 3), PageRequest.of(0, 10));

        // then
        Assertions.assertThat(actual.get()).hasSize(10);
    }

    @Test
    @DisplayName("특정 기간 내 주문 수가 반환된다.")
    void getCountOrdersInPeriod() {
        // when
        long actual = queryRepository.getCountOfOrdersInPeriod(LocalDate.of(
                2023,
                1,
                1
        ), LocalDate.of(2023, 1, 3));

        // then
        Assertions.assertThat(actual).isEqualTo(12);
    }


}