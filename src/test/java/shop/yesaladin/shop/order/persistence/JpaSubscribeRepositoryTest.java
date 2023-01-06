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
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaSubscribeRepository subscribeRepository;

    private MemberGrade memberGrade;
    private Member member;
    private MemberAddress memberAddress;
    private SubscribeProduct subscribeProduct;
    private Subscribe subscribe;

    @BeforeEach
    void setUp() {
        memberGrade = DummyMemberGrade.memberGrade;
        member = DummyMember.member(memberGrade);
        memberAddress = DummyMemberAddress.address(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();
        subscribe = DummySubscribe.subscribe(memberAddress, member, subscribeProduct);
    }

    @Test
    void save() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);
        //when
        Subscribe savedSubscribe = subscribeRepository.save(subscribe);

        //then
        assertThat(savedSubscribe).isNotNull();
    }

    @Test
    void findById() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);

        Long id = subscribeRepository.save(subscribe).getId();

        //when
        Optional<Subscribe> foundSubscribe = subscribeRepository.findById(id);

        //then
        assertThat(foundSubscribe).isPresent();
        assertThat(foundSubscribe.get().getId()).isEqualTo(id);
    }
}