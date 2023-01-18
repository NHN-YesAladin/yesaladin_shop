package shop.yesaladin.shop.payment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
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
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentCard;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;

@AutoConfigureRestDocs
@WebMvcTest(QueryPaymentController.class)
class QueryPaymentControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryPaymentService queryPaymentService;

    private Payment payment;
    private MemberOrder memberOrder;
    private String paymentId = "000000000001";
    private Long orderId = 1L;

    @BeforeEach
    void setUp() {
        Member member = DummyMember.member();
        MemberAddress memberAddress = DummyMemberAddress.address(member);
        memberOrder = MemberOrder.builder()
                .id(orderId)
                .orderNumber("orderNumber")
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
    void getPaymentByOrderId() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        PaymentCompleteSimpleResponseDto responseDto = PaymentCompleteSimpleResponseDto.fromEntity(
                payment);
        when(queryPaymentService.findByOrderId(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/payments/{orderId}",
                memberOrder.getId()
        ).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId", equalTo(payment.getId())))
//                .andExpect(jsonPath("$.approvedDateTime", equalTo(payment.getApprovedDatetime())))
                .andExpect(jsonPath("$.orderId", equalTo(payment.getOrder().getId().intValue())))
                .andExpect(jsonPath("$.cardNumber", equalTo(payment.getPaymentCard().getNumber())))
                .andExpect(jsonPath(
                        "$.cardAcquirerCode",
                        equalTo(payment.getPaymentCard().getAcquirerCode())
                ));
        verify(queryPaymentService, times(1)).findByOrderId(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(orderId);

        perform.andDo(document(
                "get-payment-by-orderId",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("orderId").description("결제정보를 확인하고자 하는 주문의 아이디")
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
                        fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 아이디"),
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