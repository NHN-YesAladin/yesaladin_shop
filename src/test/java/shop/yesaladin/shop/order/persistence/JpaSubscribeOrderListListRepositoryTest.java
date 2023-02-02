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
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeOrderListListRepositoryTest {

    private final boolean isTransported = false;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaSubscribeOrderListRepository subscribeOrderListRepository;
    private Subscribe subscribe;
    private MemberOrder memberOrder;

    @BeforeEach
    void setUp() {
        Member member = DummyMember.member();
        entityManager.persist(member);

        MemberAddress memberAddress = DummyMemberAddress.address(member);
        entityManager.persist(memberAddress);

        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        entityManager.persist(memberOrder);

        SubscribeProduct subscribeProduct = DummySubscribeProduct.subscribeProduct();
        entityManager.persist(subscribeProduct);

        subscribe = DummyOrder.subscribe(member, memberAddress, subscribeProduct);
        entityManager.persist(subscribe);
    }

    @Test
    void save() {
        //given
        SubscribeOrderList subscribeOrderList = createSubscribeOrderList();

        //when
        SubscribeOrderList actual = subscribeOrderListRepository.save(subscribeOrderList);

        //then
        assertThat(actual.isTransported()).isEqualTo(isTransported);
        assertThat(actual.getSubscribe().getExpectedDay()).isEqualTo(subscribe.getExpectedDay());
        assertThat(actual.getSubscribe()
                .getIntervalMonth()).isEqualTo(subscribe.getIntervalMonth());
        assertThat(actual.getSubscribe()
                .getNextRenewalDate()
                .getYear()).isEqualTo(subscribe.getNextRenewalDate().getYear());
        assertThat(actual.getSubscribe()
                .getNextRenewalDate()
                .getMonthValue()).isEqualTo(subscribe.getNextRenewalDate().getMonthValue());
        assertThat(actual.getSubscribe()
                .getNextRenewalDate()
                .getDayOfMonth()).isEqualTo(subscribe.getNextRenewalDate().getDayOfMonth());
        assertThat(actual.getSubscribe()
                .getSubscribeProduct()
                .getISSN()).isEqualTo(subscribe.getSubscribeProduct().getISSN());
    }

    @Test
    void findById() {
        //given
        SubscribeOrderList subscribeOrderList = createSubscribeOrderList();
        entityManager.persist(subscribeOrderList);

        long id = subscribeOrderList.getId();

        //when
        Optional<SubscribeOrderList> actual = subscribeOrderListRepository.findById(id);

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(id);
        assertThat(actual.get().isTransported()).isEqualTo(subscribeOrderList.isTransported());
        assertThat(actual.get().getSubscribe()).isSameAs(subscribeOrderList.getSubscribe());
        assertThat(actual.get().getMemberOrder()).isSameAs(subscribeOrderList.getMemberOrder());
    }

    SubscribeOrderList createSubscribeOrderList() {
        return SubscribeOrderList.builder()
                .isTransported(isTransported)
                .subscribe(subscribe)
                .memberOrder(memberOrder)
                .build();
    }
}
