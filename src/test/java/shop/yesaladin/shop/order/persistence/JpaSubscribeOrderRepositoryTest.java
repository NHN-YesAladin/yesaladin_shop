package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.domain.dummy.MemberAddress;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaOrderCommandRepository<SubscribeOrder> subscribeOrderRepository;

    private String orderNumber = "1234-5678";
    private LocalDateTime orderDateTime = LocalDateTime.now();
    private LocalDate expectedTransportDate = LocalDate.now();
    private boolean isHidden = false;
    private long usedPoint = 0;
    private int shippingFee = 0;
    private int wrappingFee = 0;
    private OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
    private boolean isTransported = false;
    private LocalDate expectedDate = LocalDate.now();
    private Subscribe subscribe;

    private SubscribeOrder subscribeOrder;

    @BeforeEach
    void setUp() {
        MemberGrade memberGrade = DummyMemberGrade.memberGrade;
        Member member = DummyMember.member(memberGrade);
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        SubscribeProduct subscribeProduct = DummySubscribeProduct.subscribeProduct();
        subscribe = DummySubscribe.subscribe(memberAddress, member, subscribeProduct);

        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);

        subscribeOrder = createSubscribeOrder();
    }

    @Test
    void save() {
        //when
        SubscribeOrder savedOrder = subscribeOrderRepository.save(subscribeOrder);

        //then
        assertThat(savedOrder.getOrderNumber()).isEqualTo(orderNumber);
        assertThat(savedOrder.getExpectedTransportDate()).isEqualTo(expectedTransportDate);
        assertThat(savedOrder.isHidden()).isEqualTo(isHidden);
        assertThat(savedOrder.getUsedPoint()).isEqualTo(usedPoint);
        assertThat(savedOrder.getShippingFee()).isEqualTo(shippingFee);
        assertThat(savedOrder.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(savedOrder.getOrderCode()).isEqualTo(orderCode);
        assertThat(savedOrder.isTransported()).isEqualTo(isTransported);
        assertThat(savedOrder.getExpectedDate()).isEqualTo(expectedDate);
        assertThat(savedOrder.getSubscribe()).isEqualTo(subscribe);
    }

    SubscribeOrder createSubscribeOrder() {
        return SubscribeOrder.builder()
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(isHidden)
                .usedPoint(usedPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .orderCode(orderCode)
                .isTransported(isTransported)
                .expectedDate(expectedDate)
                .subscribe(subscribe)
                .build();
    }
}