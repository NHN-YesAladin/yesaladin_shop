package shop.yesaladin.shop.payment.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.dummy.DummyPayment;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCommandPaymentRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaCommandPaymentRepository jpaCommandPaymentRepository;

    private Member member;
    private MemberAddress memberAddress;
    private MemberOrder memberOrder;
    private Payment payment;
    private String paymentId = "000000000001";


    @BeforeEach
    void setUp() {
        member = DummyMember.member();
        memberAddress = DummyMemberAddress.address(member);

        entityManager.persist(member);
        entityManager.persist(memberAddress);

        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        entityManager.persist(memberOrder);

        payment = DummyPayment.dummyPayment(paymentId, memberOrder);
    }

    @Test
    void save() throws Exception {
        // when
        Payment save = jpaCommandPaymentRepository.save(payment);

        // then
        assertThat(save.getId()).isEqualTo(payment.getId());
        assertThat(save.getStatus()).isEqualTo(payment.getStatus());
        assertThat(save.getOrderName()).isEqualTo(payment.getOrderName());
    }
}
