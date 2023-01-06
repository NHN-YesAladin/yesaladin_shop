package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
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
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaOrderRepository<SubscribeOrder> subscribeOrderRepository;

    private MemberGrade memberGrade;
    private Member member;
    private MemberAddress memberAddress;
    private SubscribeProduct subscribeProduct;
    private Subscribe subscribe;
    private SubscribeOrder order;

    @BeforeEach
    void setUp() {
        memberGrade = DummyMemberGrade.memberGrade;
        member = DummyMember.member(memberGrade);
        memberAddress = DummyMemberAddress.address(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();
        subscribe = DummySubscribe.subscribe(memberAddress, member, subscribeProduct);

        order = DummyOrder.subscribeOrder(subscribe);
    }

    @Test
    void save() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);

        //when
        SubscribeOrder savedOrder = subscribeOrderRepository.save(order);

        //then
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void findById() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);

        Long id = subscribeOrderRepository.save(order).getId();

        //when
        Optional<SubscribeOrder> foundOrder = subscribeOrderRepository.findById(id);

        //then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(id);
    }
}