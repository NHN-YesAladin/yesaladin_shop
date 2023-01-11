package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaSubscribeRepository subscribeRepository;

    private int intervalMonth = 6;
    private LocalDate nextRenewalDate = LocalDate.now();
    private MemberAddress memberAddress;
    private Member member;
    private SubscribeProduct subscribeProduct;

    private Subscribe subscribe;

    @BeforeEach
    void setUp() {
        member = DummyMember.member();
        memberAddress = DummyMemberAddress.address(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();

        entityManager.persist(member);
        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);

        subscribe = Subscribe.builder()
                .intervalMonth(intervalMonth)
                .nextRenewalDate(nextRenewalDate)
                .memberAddress(memberAddress)
                .member(member)
                .subscribeProduct(subscribeProduct)
                .build();
    }

    @Test
    void save() {
        //when
        Subscribe savedSubscribe = subscribeRepository.save(subscribe);

        //then
        assertThat(savedSubscribe.getIntervalMonth()).isEqualTo(intervalMonth);
        assertThat(savedSubscribe.getNextRenewalDate()).isEqualTo(nextRenewalDate);
        assertThat(savedSubscribe.getMemberAddress()).isEqualTo(memberAddress);
        assertThat(savedSubscribe.getMember()).isEqualTo(member);
        assertThat(savedSubscribe.getSubscribeProduct()).isEqualTo(subscribeProduct);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(subscribe);
        Long id = subscribe.getId();

        //when
        Optional<Subscribe> foundSubscribe = subscribeRepository.findById(id);

        //then
        assertThat(foundSubscribe).isPresent();
        assertThat(foundSubscribe.get().getId()).isEqualTo(id);
        assertThat(foundSubscribe.get().getIntervalMonth()).isEqualTo(intervalMonth);
        assertThat(foundSubscribe.get().getNextRenewalDate()).isEqualTo(nextRenewalDate);
        assertThat(foundSubscribe.get().getMemberAddress()).isEqualTo(memberAddress);
        assertThat(foundSubscribe.get().getMember()).isEqualTo(member);
        assertThat(foundSubscribe.get().getSubscribeProduct()).isEqualTo(subscribeProduct);
    }
}