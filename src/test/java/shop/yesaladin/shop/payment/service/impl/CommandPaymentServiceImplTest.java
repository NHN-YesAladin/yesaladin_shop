package shop.yesaladin.shop.payment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;
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
    private QueryPaymentRepository queryPaymentRepository;
    private QueryOrderService orderService;
    private CommandOrderStatusChangeLogService commandOrderStatusChangeLogService;
    private RestTemplate restTemplate;
    private ApplicationEventPublisher applicationEventPublisher;
    private ObjectMapper mapper = new ObjectMapper();

    private MemberOrder memberOrder;
    private NonMemberOrder nonMemberOrder;

    private String jsonBodyEasyPay = "{\"mId\":\"tvivarepublica\",\"lastTransactionKey\":\"CB523BC9E9942CB203A0958ECE82A2FF\",\"paymentKey\":\"dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y\",\"orderId\":\"zaIkFmBcYW4k_J9rOl0M2b7\",\"orderName\":\"토스 티셔츠 외 2건\",\"taxExemptionAmount\":0,\"status\":\"DONE\",\"requestedAt\":\"2023-01-23T23:59:11+09:00\",\"approvedAt\":\"2023-01-23T23:59:46+09:00\",\"useEscrow\":false,\"cultureExpense\":false,\"card\":{\"issuerCode\":\"24\",\"acquirerCode\":\"21\",\"number\":\"53275080****562*\",\"installmentPlanMonths\":0,\"isInterestFree\":false,\"interestPayer\":null,\"approveNo\":\"00000000\",\"useCardPoint\":false,\"cardType\":\"신용\",\"ownerType\":\"개인\",\"acquireStatus\":\"READY\",\"amount\":15000},\"virtualAccount\":null,\"transfer\":null,\"mobilePhone\":null,\"giftCertificate\":null,\"cashReceipt\":null,\"discount\":null,\"cancels\":null,\"secret\":\"ps_Gv6LjeKD8aa1PB5wXk0e8wYxAdXy\",\"type\":\"NORMAL\",\"easyPay\":{\"provider\":\"토스페이\",\"amount\":0,\"discountAmount\":0},\"country\":\"KR\",\"failure\":null,\"isPartialCancelable\":true,\"receipt\":{\"url\":\"https://dashboard.tosspayments.com/sales-slip?transactionId=t2PBzrTfEmg349R5W6nkrow%2BUzDofNfXtzOl46Bf9T6DwV8hRWVYUIkPBiT4to%2Bg&ref=PX\"},\"checkout\":{\"url\":\"https://api.tosspayments.com/v1/payments/dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y/checkout\"},\"currency\":\"KRW\",\"totalAmount\":15000,\"balanceAmount\":15000,\"suppliedAmount\":13636,\"vat\":1364,\"taxFreeAmount\":0,\"method\":\"간편결제\",\"version\":\"2022-11-16\"}";
    private String jsonBodyCard = "{\"mId\":\"tvivarepublica4\",\"lastTransactionKey\":\"CDDE9497C9D712D36283B59CBB240CB2\",\"paymentKey\":\"Kl56WYb7w4vZnjEJeQVxyjydXgknOYrPmOoBN0k12dzgRG9p\",\"orderId\":\"v0AmjbyGWFc82lZWRYef3AWzz\",\"orderName\":\"토스티셔츠 외 2건\",\"taxExemptionAmount\":0,\"status\":\"DONE\",\"requestedAt\":\"2023-02-10T23:18:22+09:00\",\"approvedAt\":\"2023-02-10T23:18:22+09:00\",\"useEscrow\":false,\"cultureExpense\":false,\"card\":{\"issuerCode\":\"4V\",\"acquirerCode\":\"21\",\"number\":\"43301234****123*\",\"installmentPlanMonths\":0,\"isInterestFree\":false,\"interestPayer\":null,\"approveNo\":\"00000000\",\"useCardPoint\":false,\"cardType\":\"신용\",\"ownerType\":\"개인\",\"acquireStatus\":\"READY\",\"amount\":15000},\"virtualAccount\":null,\"transfer\":null,\"mobilePhone\":null,\"giftCertificate\":null,\"cashReceipt\":null,\"discount\":null,\"cancels\":null,\"secret\":null,\"type\":\"NORMAL\",\"easyPay\":null,\"country\":\"KR\",\"failure\":null,\"isPartialCancelable\":false,\"receipt\":{\"url\":\"https://dashboard.tosspayments.com/sales-slip?transactionId=sYdhdtKe4uBOItIzdbE5DvcJU1OiNuvPPXxBA7YDam82qaaQr%2BpNgjZYFgtXO6Bs&ref=PX\"},\"checkout\":{\"url\":\"https://api.tosspayments.com/v1/payments/Kl56WYb7w4vZnjEJeQVxyjydXgknOYrPmOoBN0k12dzgRG9p/checkout\"},\"currency\":\"KRW\",\"totalAmount\":15000,\"balanceAmount\":15000,\"suppliedAmount\":13636,\"vat\":1364,\"taxFreeAmount\":0,\"method\":\"카드\",\"version\":\"2022-11-16\"}";
    private String jsonBodyFail = "{\"message\":\"결제실패\",\"code\":\"CANCELED\"}";

    @BeforeEach
    void setUp() {
        paymentRepository = mock(CommandPaymentRepository.class);
        orderService = mock(QueryOrderService.class);
        restTemplate = mock(RestTemplate.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        queryPaymentRepository = mock(QueryPaymentRepository.class);
        commandOrderStatusChangeLogService = mock(CommandOrderStatusChangeLogService.class);

        paymentService = new CommandPaymentServiceImpl(restTemplate,
                paymentRepository,
                queryPaymentRepository,
                orderService,
                commandOrderStatusChangeLogService,
                applicationEventPublisher
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

        nonMemberOrder = NonMemberOrder.builder()
                .name("nonMemberOrder")
                .orderNumber(orderNumber + "n")
                .orderDateTime(LocalDateTime.now())
                .expectedTransportDate(LocalDate.now())
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(0)
                .totalAmount(10000L)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address("서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)")
                .nonMemberName("김몽머")
                .phoneNumber("01012341234")
                .build();
    }

    @Test
    @DisplayName("회원 & 구독 주문시, 결제 성공하여 특정 정보 반환 성공")
    void confirmTossRequest_memberOrder() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "Kl56WYb7w4vZnjEJeQVxyjydXgknOYrPmOoBN0k12dzgRG9p",
                "v0AmjbyGWFc82lZWRYef3AWzz",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyCard);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), memberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        OrderPaymentResponseDto orderPaymentResponseDto = new OrderPaymentResponseDto(memberOrder.getMember()
                .getName(), memberOrder.getMemberAddress().getAddress());
        when(orderService.getPaymentDtoByMemberOrderId(anyLong())).thenReturn(
                orderPaymentResponseDto);

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));

        // when
        PaymentCompleteSimpleResponseDto responseDto = paymentService.confirmTossRequest(requestDto);

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(responseDto.getCardNumber()).isEqualTo(payment.getPaymentCard().getNumber());
        assertThat(responseDto.getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());
        assertThat(responseDto.getOrdererName()).isEqualTo(memberOrder.getMember().getName());
        assertThat(responseDto.getOrderAddress()).isEqualTo(memberOrder.getMemberAddress()
                .getAddress());

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());

        verify(orderService, times(1)).getPaymentDtoByMemberOrderId(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(memberOrder.getId());
    }

    @Test
    @DisplayName("비회원 주문시, 결제 성공하여 특정 정보 반환 성공")
    void confirmTossRequest_noneMemberOrder() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "Kl56WYb7w4vZnjEJeQVxyjydXgknOYrPmOoBN0k12dzgRG9p",
                "v0AmjbyGWFc82lZWRYef3AWzz",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyCard);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(nonMemberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), nonMemberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));
        // when
        PaymentCompleteSimpleResponseDto responseDto = paymentService.confirmTossRequest(requestDto);

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(responseDto.getCardNumber()).isEqualTo(payment.getPaymentCard().getNumber());
        assertThat(responseDto.getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());
        assertThat(responseDto.getOrdererName()).isEqualTo(nonMemberOrder.getNonMemberName());
        assertThat(responseDto.getOrderAddress()).isEqualTo(nonMemberOrder.getAddress());

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());
    }

    @Test
    @DisplayName("회원 & 구독 주문시, 간편 결제 성공하여 특정 정보 반환 성공")
    void confirmTossRequest_easyPay_memberOrder() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyEasyPay);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), memberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        OrderPaymentResponseDto orderPaymentResponseDto = new OrderPaymentResponseDto(memberOrder.getMember()
                .getName(), memberOrder.getMemberAddress().getAddress());
        when(orderService.getPaymentDtoByMemberOrderId(anyLong())).thenReturn(
                orderPaymentResponseDto);

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));

        // when
        PaymentCompleteSimpleResponseDto responseDto = paymentService.confirmTossRequest(requestDto);

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(responseDto.getEasyPayProvider()).isEqualTo(payment.getPaymentEasyPay()
                .getProvider());
        assertThat(responseDto.getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());
        assertThat(responseDto.getOrdererName()).isEqualTo(memberOrder.getMember().getName());
        assertThat(responseDto.getOrderAddress()).isEqualTo(memberOrder.getMemberAddress()
                .getAddress());

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());

        verify(orderService, times(1)).getPaymentDtoByMemberOrderId(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(memberOrder.getId());
    }

    @Test
    @DisplayName("비회원 주문시, 간편결제 성공하여 특정 정보 반환 성공")
    void confirmTossRequest_easyPay_noneMemberOrder() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyEasyPay);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(nonMemberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), nonMemberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));

        // when
        PaymentCompleteSimpleResponseDto responseDto = paymentService.confirmTossRequest(requestDto);

        // then
        assertThat(responseDto.getPaymentId()).isEqualTo(payment.getId());
        assertThat(responseDto.getEasyPayProvider()).isEqualTo(payment.getPaymentEasyPay()
                .getProvider());
        assertThat(responseDto.getOrderNumber()).isEqualTo(payment.getOrder().getOrderNumber());
        assertThat(responseDto.getOrdererName()).isEqualTo(nonMemberOrder.getNonMemberName());
        assertThat(responseDto.getOrderAddress()).isEqualTo(nonMemberOrder.getAddress());

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());
    }

    @Test
    @DisplayName("토스측 에러로 결제 실패")
    void confirmTossRequest_notOk_fail() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyFail);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        when(paymentRepository.save(any())).thenReturn(Payment.builder().build());

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));
        // when
        // then
        assertThatThrownBy(() -> paymentService.confirmTossRequest(requestDto)).isInstanceOf(
                PaymentFailException.class);

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(requestDto.getOrderId());

        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("사용자측 요청 에러로 토스 결제 실패")
    void confirmTossRequest_userFailure_fail() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyFail);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenThrow(
                RestClientException.class);

        when(orderService.getOrderByNumber(any())).thenReturn(memberOrder);

        when(paymentRepository.save(any())).thenReturn(Payment.builder().build());

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));

        // when
        // then
        assertThatThrownBy(() -> paymentService.confirmTossRequest(requestDto)).isInstanceOf(
                PaymentFailException.class);

        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(requestDto.getOrderId());

        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("비회원 주문시, 간편결제 성공했지만 append에서 오류 발생")
    void confirmTossRequest_easyPay_noneMemberOrder_appendLog_fail() throws Exception {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y",
                "zaIkFmBcYW4k_J9rOl0M2b7",
                15000L
        );

        JsonNode jsonNode = mapper.readTree(jsonBodyEasyPay);
        ResponseEntity<JsonNode> exchange = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class))).thenReturn(exchange);

        when(orderService.getOrderByNumber(any())).thenReturn(nonMemberOrder);

        Payment payment = Payment.toEntity(exchange.getBody(), nonMemberOrder);
        when(paymentRepository.save(any())).thenReturn(payment);

        doNothing().when(applicationEventPublisher).publishEvent(any());
        doThrow(new ClientException(ErrorCode.ORDER_BAD_REQUEST, "잘못된 주문 상태 변경 요청입니다.")).when(
                        commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.DEPOSIT));

        doNothing().when(commandOrderStatusChangeLogService)
                .appendOrderStatusChangeLog(any(), any(), eq(OrderStatusCode.READY));

        // when
        assertThatCode(() -> paymentService.confirmTossRequest(requestDto)).isInstanceOf(
                ClientException.class).hasMessageContaining("잘못된 주문 상태 변경 요청입니다.");

        // then
        verify(orderService, times(1)).getOrderByNumber(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(exchange.getBody()
                .get("orderId")
                .asText());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        assertThat(paymentArgumentCaptor.getValue().getId()).isEqualTo(payment.getId());
    }
}
