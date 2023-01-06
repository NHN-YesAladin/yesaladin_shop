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
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaOrderRepository<MemberOrder> memberOrderRepository;

    private MemberGrade memberGrade;
    private Member member;
    private MemberAddress memberAddress;
    private MemberOrder order;

    @BeforeEach
    void setUp() {
        memberGrade = DummyMemberGrade.memberGrade;
        member = DummyMember.member(memberGrade);
        memberAddress = DummyMemberAddress.address(member);
        order = DummyOrder.memberOrder(member, memberAddress);
    }

    @Test
    void save() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);

        //when
        MemberOrder savedOrder = memberOrderRepository.save(order);

        //then
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void findById() {
        //given
        entityManager.persist(memberGrade);
        entityManager.persist(member);
        entityManager.persist(memberAddress);

        Long id = memberOrderRepository.save(order).getId();

        //when
        Optional<MemberOrder> foundOrder = memberOrderRepository.findById(id);

        //then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(id);
    }
}