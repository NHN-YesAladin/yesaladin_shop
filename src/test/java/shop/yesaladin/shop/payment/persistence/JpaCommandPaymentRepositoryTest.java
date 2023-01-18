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
import shop.yesaladin.shop.payment.domain.model.PaymentCancel;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCancel;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCard;


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
    private PaymentCard paymentCard;
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

        payment = DummyPayment.payment(paymentId, memberOrder);
        paymentCard = DummyPaymentCard.paymentCard(payment);
        payment.setPaymentCard(paymentCard);
    }

    @Test
    @DisplayName(" 결제 완료 이후 상황 - payment에 card 정보를 set 한 상태로 insert")
    void save_insertPayment_withCard() throws Exception {
        // when
        Payment save = jpaCommandPaymentRepository.save(payment);

        // then
        assertThat(save.getOrder()).isEqualTo(memberOrder);
        assertThat(save.getPaymentCard().getAmount()).isEqualTo(paymentCard.getAmount());
        assertThat(save.getId()).isEqualTo(payment.getId());
        assertThat(save.getStatus()).isEqualTo(payment.getStatus());
        assertThat(save.getOrderName()).isEqualTo(payment.getOrderName());
    }

    @Test
    @DisplayName(" 결제 취소 신청시 - payment를 불러와 cancel 을 set 하고 save")
    void save_updatePayment_withCancel() throws Exception {
        // given
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();

        // when
        Payment foundPayment = entityManager.find(Payment.class, paymentId);
        PaymentCancel paymentCancel = DummyPaymentCancel.paymentCancel(foundPayment);
        foundPayment.setStatus(PaymentCode.CANCELED);
        foundPayment.setPaymentCancel(paymentCancel);
        entityManager.persist(foundPayment);
        entityManager.flush();
        entityManager.clear();

        // then
        Payment found = entityManager.find(Payment.class, payment.getId());
        assertThat(found.getStatus()).isEqualTo(PaymentCode.CANCELED);
        assertThat(found.getPaymentCancel()
                .getCancelAmount()).isEqualTo(paymentCancel.getCancelAmount());
        assertThat(found.getPaymentCancel()
                .getCancelReason()).isEqualTo(paymentCancel.getCancelReason());
    }

    @Test
    @DisplayName("CascadeType.ALL이 안걸려있는 엔티티가 삭제 될 경우, 해당 엔티티만 삭제 확인 ")
    void delete_onlyPaymentCard() throws Exception {
        // given
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();

        // when
        PaymentCard card = entityManager.find(PaymentCard.class, payment.getId());
        entityManager.remove(card);
        entityManager.flush();
        entityManager.clear();

        // then
        Payment found = entityManager.find(Payment.class, payment.getId());
        assertThat(found.getPaymentCard()).isNull();
        assertThat(found.getId()).isEqualTo(payment.getId());
    }

    @Test
    @DisplayName("CascadeType.ALL가 걸린 엔티티가 삭제 될 경우, 해당 엔티티 아래에있는 엔티티까지 삭제 확인 ")
    void delete_payment_thenBothDeleted() throws Exception {
        // given
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();

        // when
        Payment foundPayment = entityManager.find(Payment.class, payment.getId());
        jpaCommandPaymentRepository.delete(foundPayment);
        entityManager.flush();
        entityManager.clear();

        // then
        Payment nullPayment = entityManager.find(Payment.class, payment.getId());
        PaymentCard nullCard = entityManager.find(PaymentCard.class, payment.getId());
        assertThat(nullPayment).isNull();
        assertThat(nullCard).isNull();
    }
}
