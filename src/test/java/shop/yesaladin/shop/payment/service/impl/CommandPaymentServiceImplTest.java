package shop.yesaladin.shop.payment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;

/**
 * @author 배수한
 * @since 1.0
 */
class CommandPaymentServiceImplTest {

    private CommandPaymentService paymentService;
    private CommandPaymentRepository paymentRepository;
    private QueryOrderService orderService;
    private RestTemplate restTemplate;
    private ObjectMapper mapper = new ObjectMapper();

    private MemberOrder memberOrder;

    private String jsonBody = "{\"mId\":\"tvivarepublica\",\"lastTransactionKey\":\"CB523BC9E9942CB203A0958ECE82A2FF\",\"paymentKey\":\"dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y\",\"orderId\":\"zaIkFmBcYW4k_J9rOl0M2b7\",\"orderName\":\"토스 티셔츠 외 2건\",\"taxExemptionAmount\":0,\"status\":\"DONE\",\"requestedAt\":\"2023-01-23T23:59:11+09:00\",\"approvedAt\":\"2023-01-23T23:59:46+09:00\",\"useEscrow\":false,\"cultureExpense\":false,\"card\":{\"issuerCode\":\"24\",\"acquirerCode\":\"21\",\"number\":\"53275080****562*\",\"installmentPlanMonths\":0,\"isInterestFree\":false,\"interestPayer\":null,\"approveNo\":\"00000000\",\"useCardPoint\":false,\"cardType\":\"신용\",\"ownerType\":\"개인\",\"acquireStatus\":\"READY\",\"amount\":15000},\"virtualAccount\":null,\"transfer\":null,\"mobilePhone\":null,\"giftCertificate\":null,\"cashReceipt\":null,\"discount\":null,\"cancels\":null,\"secret\":\"ps_Gv6LjeKD8aa1PB5wXk0e8wYxAdXy\",\"type\":\"NORMAL\",\"easyPay\":{\"provider\":\"토스페이\",\"amount\":0,\"discountAmount\":0},\"country\":\"KR\",\"failure\":null,\"isPartialCancelable\":true,\"receipt\":{\"url\":\"https://dashboard.tosspayments.com/sales-slip?transactionId=t2PBzrTfEmg349R5W6nkrow%2BUzDofNfXtzOl46Bf9T6DwV8hRWVYUIkPBiT4to%2Bg&ref=PX\"},\"checkout\":{\"url\":\"https://api.tosspayments.com/v1/payments/dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y/checkout\"},\"currency\":\"KRW\",\"totalAmount\":15000,\"balanceAmount\":15000,\"suppliedAmount\":13636,\"vat\":1364,\"taxFreeAmount\":0,\"method\":\"간편결제\",\"version\":\"2022-11-16\"}";

    @BeforeEach
    void setUp() {
        paymentRepository = mock(CommandPaymentRepository.class);
        orderService = mock(QueryOrderService.class);
        restTemplate = mock(RestTemplate.class);

        paymentService = new CommandPaymentServiceImpl(
                restTemplate,
                paymentRepository,
                orderService
        );

        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        memberOrder = DummyOrder.memberOrder(member, memberAddress);

    }

    @Test
    void confirmTossRequest() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBody);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(
                any(),
                any(),
                any(),
                eq(JsonNode.class)
        )).thenReturn(
                exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), memberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        // when
        PaymentCompleteSimpleResponseDto responseDto = paymentService.confirmTossRequest(requestDto);

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(responseDto.getCardNumber()).isEqualTo(payment.getPaymentCard().getNumber());
        assertThat(responseDto.getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());
    }

    @Test
    void confirmTossRequest_notOk_fail() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBody);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(
                any(),
                any(),
                any(),
                eq(JsonNode.class)
        )).thenReturn(
                exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), memberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        // when
        // then
        assertThatThrownBy(() -> paymentService.confirmTossRequest(requestDto)).isInstanceOf(
                PaymentFailException.class);

        verify(orderService, never()).getOrderByNumber(any());

        verify(paymentRepository, never()).save(any());
    }
}
