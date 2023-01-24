package shop.yesaladin.shop.payment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCard;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCard;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;


@AutoConfigureRestDocs
@WebMvcTest(CommandPaymentController.class)
class CommandPaymentControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandPaymentService paymentService;

    private MemberOrder memberOrder;
    private Payment payment;
    private Long orderId = 1L;
    private String orderNumber = "zaIkFmBcYW4k_J9rOl0M2b7";
    private String paymentId = "dJv2eBNjG0Poxy1XQL8RJqpXm9gkOk87nO5Wmlg96RKwZz4Y";

    @BeforeEach
    void setUp() {
        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        memberOrder = MemberOrder.builder()
                .id(orderId)
                .orderNumber(orderNumber)
                .name("memberOrder")
                .orderDateTime(LocalDateTime.now())
                .expectedTransportDate(LocalDate.now())
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(0)
                .wrappingFee(0)
                .orderCode(OrderCode.MEMBER_ORDER)
                .memberAddress(memberAddress)
                .member(member)
                .build();

        payment = DummyPayment.payment(paymentId, memberOrder);

        PaymentCard paymentCard = DummyPaymentCard.paymentCard(payment);
        payment.setPaymentCard(paymentCard);
    }

    @Test
    @DisplayName("결제 승인 성공")
    void confirmPayment() throws Exception {
        // given
        ArgumentCaptor<PaymentRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                PaymentRequestDto.class);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                paymentId,
                orderNumber,
                15000L
        );
        PaymentCompleteSimpleResponseDto responseDto = PaymentCompleteSimpleResponseDto.fromEntity(
                payment);
        when(paymentService.confirmTossRequest(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/v1/payments/confirm").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.paymentId", equalTo(responseDto.getPaymentId())))
                .andExpect(jsonPath("$.orderNumber", equalTo(responseDto.getOrderNumber())))
                .andExpect(jsonPath("$.cardNumber", equalTo(responseDto.getCardNumber())));

        verify(paymentService, times(1)).confirmTossRequest(dtoArgumentCaptor.capture());
        assertThat(dtoArgumentCaptor.getValue()
                .getPaymentKey()).isEqualTo(requestDto.getPaymentKey());

        perform.andDo(document(
                "confirm-payment-to-Toss",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("paymentKey").type(JsonFieldType.STRING)
                                .description("토스 페이먼츠에서 제공하는 결제 id"),
                        fieldWithPath("orderId").type(JsonFieldType.STRING)
                                .description("결제하고자 하는 주문의 번호 - id 아님"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("결제하고자하는 총 금액")
                ),
                responseFields(
                        fieldWithPath("paymentId").type(JsonFieldType.STRING)
                                .description("결제정보 아이디"),
                        fieldWithPath("method").type(JsonFieldType.STRING).description("결제 방법"),
                        fieldWithPath("currency").type(JsonFieldType.STRING).description("결제 통화"),
                        fieldWithPath("totalAmount").type(JsonFieldType.NUMBER)
                                .description("결제 총 금액"),
                        fieldWithPath("approvedDateTime").type(JsonFieldType.STRING)
                                .description("결제 승인 일시"),
                        fieldWithPath("orderNumber").type(JsonFieldType.STRING)
                                .description("주문 번호"),
                        fieldWithPath("orderName").type(JsonFieldType.STRING).description("주문명"),
                        fieldWithPath("cardCode").type(JsonFieldType.STRING).description("카드 종류"),
                        fieldWithPath("cardOwnerCode").type(JsonFieldType.STRING)
                                .description("카드 소유 구분"),
                        fieldWithPath("cardNumber").type(JsonFieldType.STRING).description("카드 번호"),
                        fieldWithPath("cardInstallmentPlanMonths").type(JsonFieldType.NUMBER)
                                .description("할부 개월 수"),
                        fieldWithPath("cardApproveNumber").type(JsonFieldType.STRING)
                                .description("카드 결제 승인 번호"),
                        fieldWithPath("cardAcquirerCode").type(JsonFieldType.STRING)
                                .description("카드 매입사")
                )
        ));
    }

}
