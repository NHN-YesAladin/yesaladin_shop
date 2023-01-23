package shop.yesaladin.shop.payment.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCard;
import shop.yesaladin.shop.payment.exception.PaymentNotFoundException;


@Slf4j
@Transactional
@SpringBootTest
class QueryDslPaymentRepositoryTest {

    @Autowired
    private QueryDslPaymentRepository queryDslPaymentRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_paymentId() {
        // when
        Payment foundPayment = queryDslPaymentRepository.findById(paymentId, null)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(foundPayment.getId()).isEqualTo(paymentId);
        assertThat(foundPayment.getStatus()).isEqualTo(payment.getStatus());

        assertThat(foundPayment.getOrder()
                .getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(foundPayment.getOrder().getName()).isEqualTo(memberOrder.getName());

        assertThat(foundPayment.getPaymentCard().getAmount()).isEqualTo(paymentCard.getAmount());
        assertThat(foundPayment.getPaymentCard()
                .getApproveNo()).isEqualTo(paymentCard.getApproveNo());
    }

    @Test
    void findById_orderId() {
        // when
        Payment foundPayment = queryDslPaymentRepository.findById(null, memberOrder.getId())
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(foundPayment.getId()).isEqualTo(paymentId);
        assertThat(foundPayment.getStatus()).isEqualTo(payment.getStatus());

        assertThat(foundPayment.getOrder()
                .getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(foundPayment.getOrder().getName()).isEqualTo(memberOrder.getName());

        assertThat(foundPayment.getPaymentCard().getAmount()).isEqualTo(paymentCard.getAmount());
        assertThat(foundPayment.getPaymentCard()
                .getApproveNo()).isEqualTo(paymentCard.getApproveNo());
    }

    @Test
    void findById_both_paymentId_orderId() {
        // when
        Payment foundPayment = queryDslPaymentRepository.findById(paymentId, memberOrder.getId())
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(foundPayment.getId()).isEqualTo(paymentId);
        assertThat(foundPayment.getStatus()).isEqualTo(payment.getStatus());

        assertThat(foundPayment.getOrder()
                .getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(foundPayment.getOrder().getName()).isEqualTo(memberOrder.getName());

        assertThat(foundPayment.getPaymentCard().getAmount()).isEqualTo(paymentCard.getAmount());
        assertThat(foundPayment.getPaymentCard()
                .getApproveNo()).isEqualTo(paymentCard.getApproveNo());
    }

    @Test
    void findById_wrongPaymentId_fail() {
        // when
        String wrongId = "Wrong ID";
        Optional<Payment> wrongPayment = queryDslPaymentRepository.findById(
                wrongId,
                memberOrder.getId()
        );

        // then
        assertThatThrownBy(() -> wrongPayment.orElseThrow(() -> new PaymentNotFoundException(wrongId))).isInstanceOf(
                PaymentNotFoundException.class);
    }

    @Test
    void findById_wrongOrderId_fail() {
        // when
        Long notFoundId = 1000L;
        Optional<Payment> wrongPayment = queryDslPaymentRepository.findById(
                paymentId,
                notFoundId
        );

        // then
        assertThatThrownBy(() -> wrongPayment.orElseThrow(() -> new PaymentNotFoundException(
                notFoundId))).isInstanceOf(
                PaymentNotFoundException.class);
    }

    @Test
    void findSimpleDtoById_paymentId() {
        // when
        PaymentCompleteSimpleResponseDto responseDto = queryDslPaymentRepository.findSimpleDtoById(
                        paymentId,
                        null
                )
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(paymentId);
        assertThat(responseDto.getMethod()).isEqualTo(payment.getMethod());

        assertThat(responseDto.getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(responseDto.getOrderName()).isEqualTo(memberOrder.getName());

        assertThat(responseDto.getCardCode()).isEqualTo(paymentCard.getCardCode());
        assertThat(responseDto.getCardNumber()).isEqualTo(paymentCard.getNumber());

        log.info("result : {}", responseDto);
    }

    @Test
    void findSimpleDtoById_orderId() {
        // when
        PaymentCompleteSimpleResponseDto responseDto = queryDslPaymentRepository.findSimpleDtoById(
                        null,
                        memberOrder.getId()
                )
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(paymentId);
        assertThat(responseDto.getMethod()).isEqualTo(payment.getMethod());

        assertThat(responseDto.getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(responseDto.getOrderName()).isEqualTo(memberOrder.getName());

        assertThat(responseDto.getCardCode()).isEqualTo(paymentCard.getCardCode());
        assertThat(responseDto.getCardNumber()).isEqualTo(paymentCard.getNumber());
    }

    @Test
    void findSimpleDtoById_both_paymentId_orderId() {
        // when
        PaymentCompleteSimpleResponseDto responseDto = queryDslPaymentRepository.findSimpleDtoById(
                        paymentId,
                        memberOrder.getId()
                )
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(paymentId);
        assertThat(responseDto.getMethod()).isEqualTo(payment.getMethod());

        assertThat(responseDto.getOrderNumber()).isEqualTo(memberOrder.getOrderNumber());
        assertThat(responseDto.getOrderName()).isEqualTo(memberOrder.getName());

        assertThat(responseDto.getCardCode()).isEqualTo(paymentCard.getCardCode());
        assertThat(responseDto.getCardNumber()).isEqualTo(paymentCard.getNumber());
    }

    @Test
    void findSimpleDtoById_wrongPaymentId_fail() {
        // when
        String wrongId = "Wrong ID";
        Optional<PaymentCompleteSimpleResponseDto> wrongDto = queryDslPaymentRepository.findSimpleDtoById(
                wrongId,
                memberOrder.getId()
        );

        // then
        assertThatThrownBy(() -> wrongDto.orElseThrow(() -> new PaymentNotFoundException(wrongId))).isInstanceOf(
                PaymentNotFoundException.class);
    }

    @Test
    void findSimpleDtoById_wrongOrderId_fail() {
        // when
        Long notFoundId = 1000L;
        Optional<PaymentCompleteSimpleResponseDto> wrongDto = queryDslPaymentRepository.findSimpleDtoById(
                paymentId,
                notFoundId
        );

        // then
        assertThatThrownBy(() -> wrongDto.orElseThrow(() -> new PaymentNotFoundException(notFoundId))).isInstanceOf(
                PaymentNotFoundException.class);
    }
}
