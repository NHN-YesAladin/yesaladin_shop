package shop.yesaladin.shop.payment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCard;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;


class QueryPaymentServiceImplTest {

    private QueryPaymentRepository queryPaymentRepository;
    private QueryPaymentService queryPaymentService;

    private Payment payment;
    private MemberOrder memberOrder;
    private String paymentId = "000000000001";


    @BeforeEach
    void setUp() {
        queryPaymentRepository = Mockito.mock(QueryPaymentRepository.class);

        queryPaymentService = new QueryPaymentServiceImpl(
                queryPaymentRepository
        );

        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);

        String orderNumber = "20230106-3942JE8m";
        memberOrder = MemberOrder.builder()
                .id(1L)
                .orderNumber(orderNumber)
                .name("memberOrder")
                .orderDateTime(LocalDateTime.now())
                .expectedTransportDate(LocalDate.now())
                .isHidden(false)
                .usedPoint(1000L)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(9000L)
                .orderCode(OrderCode.MEMBER_ORDER)
                .memberAddress(memberAddress)
                .member(member)
                .build();

        payment = DummyPayment.payment(paymentId, memberOrder);

        PaymentCard paymentCard = DummyPaymentCard.paymentCard(payment);
        payment.setPaymentCard(paymentCard);
    }

    @Test
    void findByOrderId() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(queryPaymentRepository.findById(any(), any())).thenReturn(Optional.of(payment));

        // when
        Payment foundPayment = queryPaymentService.findByOrderId(
                memberOrder.getId());

        // then
        assertThat(foundPayment.getPaymentCard().getNumber()).isEqualTo(payment.getPaymentCard().getNumber());
        assertThat(foundPayment.getId()).isEqualTo(payment.getId());
        assertThat(foundPayment.getOrder().getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());

        verify(queryPaymentRepository, times(1)).findById(
                eq(null),
                longArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(memberOrder.getId());

    }

}
