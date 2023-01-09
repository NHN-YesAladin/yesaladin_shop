package shop.yesaladin.shop.order.persistence;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.domain.dummy.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
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
    void setUp() {
        nonMemberOrder = DummyOrder.nonMemberOrder();
        MemberGrade memberGrade = DummyMemberGrade.memberGrade;
        Member member = DummyMember.member(memberGrade);
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        SubscribeProduct subscribeProduct = DummySubscribeProduct.subscribeProduct();
        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        Subscribe subscribe = DummySubscribe.subscribe(
                memberAddress,
                member,
                subscribeProduct
        );
        subscribeOrder = DummyOrder.subscribeOrder(subscribe);
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(nonMemberOrder);
        entityManager.persist(memberOrder);
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);
        entityManager.persist(subscribeOrder);
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
        Assertions.assertThat(actual.get())
                .isInstanceOf(OrderCode.MEMBER_ORDER.getOrderClass());
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


}