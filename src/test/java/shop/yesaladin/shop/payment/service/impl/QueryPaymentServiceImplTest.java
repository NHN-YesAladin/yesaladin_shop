package shop.yesaladin.shop.payment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
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

        queryPaymentService = new QueryPaymentServiceImpl(queryPaymentRepository);

        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        memberOrder = DummyOrder.memberOrder(member, memberAddress);

        payment = DummyPayment.payment(paymentId, memberOrder);

        PaymentCard paymentCard = DummyPaymentCard.paymentCard(payment);
        payment.setPaymentCard(paymentCard);
    }

    @Test
    void findByOrderId() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        PaymentCompleteSimpleResponseDto responseDto = PaymentCompleteSimpleResponseDto.fromEntity(
                payment);
        when(queryPaymentRepository.findSimpleDtoById(any(), any())).thenReturn(Optional.of(
                responseDto));

        // when
        PaymentCompleteSimpleResponseDto returnedDto = queryPaymentService.findByOrderId(memberOrder.getId());

        // then
        assertThat(returnedDto.getCardNumber()).isEqualTo(payment.getPaymentCard().getNumber());
        assertThat(returnedDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(returnedDto.getOrderId()).isEqualTo(payment.getOrder().getId());

        verify(queryPaymentRepository, times(1)).findSimpleDtoById(
                stringArgumentCaptor.capture(),
                longArgumentCaptor.capture()
        );
        assertThat(stringArgumentCaptor.getValue()).isNull();
        assertThat(longArgumentCaptor.getValue()).isEqualTo(memberOrder.getId());
    }

}
