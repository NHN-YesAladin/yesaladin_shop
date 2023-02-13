package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.getDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderDetailsResponseDto;
import shop.yesaladin.shop.order.dto.OrderProductResponseDto;
import shop.yesaladin.shop.order.dto.OrderResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;
import shop.yesaladin.shop.payment.domain.model.PaymentEasyPay;
import shop.yesaladin.shop.payment.dto.PaymentResponseDto;
import shop.yesaladin.shop.payment.dummy.DummyPayment;
import shop.yesaladin.shop.payment.dummy.DummyPaymentEasyPay;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

@AutoConfigureRestDocs
@WebMvcTest(QueryOrderController.class)
class QueryOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryOrderService queryOrderService;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    @DisplayName("기간 내의 모든 주문 내역이 조회된다.")
    void getAllOrdersTest() throws Exception {
        // given
        Mockito.when(queryOrderService.getAllOrderListInPeriod(any(), any()))
                .thenReturn(new PageImpl<>(List.of(new OrderSummaryDto("123",
                        LocalDateTime.of(2023, 1, 1, 0, 0),
                        OrderCode.MEMBER_ORDER
                ))));

        Map<String, Object> request = Map.of("startDate",
                LocalDate.of(2023, 1, 1),
                "endDate",
                LocalDate.of(2023, 1, 2)
        );
        ArgumentCaptor<PeriodQueryRequestDto> dtoCaptor = ArgumentCaptor.forClass(
                PeriodQueryRequestDto.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        // when
        ResultActions result = mockMvc.perform(get("/v1/orders").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("size", "20")
                .param("page", "1"));

        // then
        ResultActions resultActions = result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(queryOrderService, Mockito.times(1))
                .getAllOrderListInPeriod(dtoCaptor.capture(), pageableCaptor.capture());
        PeriodQueryRequestDto actualDto = dtoCaptor.getValue();
        Pageable actualPageable = pageableCaptor.getValue();

        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(PeriodQueryRequestDto.class,
                        "startDate",
                        actualDto
                ).get())
                .isEqualTo(request.get("startDate"));
        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(PeriodQueryRequestDto.class,
                        "endDate",
                        actualDto
                ).get())
                .isEqualTo(request.get("endDate"));
        Assertions.assertThat(actualPageable.getPageSize()).isEqualTo(20);
        Assertions.assertThat(actualPageable.getPageNumber()).isEqualTo(1);

        // docs
        resultActions.andDo(document("get-all-order-in-period",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("size").description("페이지네이션 사이즈")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                requestFields(fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("조회 시작 일자")
                                .optional()
                                .attributes(getDateFormat()),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("조회 종료 일자")
                                .optional()
                                .attributes(getDateFormat())
                ),
                responseFields(fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("총 페이지 수"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지 번호"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("모든 데이터의 수"),
                        fieldWithPath("dataList").type(JsonFieldType.ARRAY)
                                .description("조회된 주문 요약 데이터 리스트"),
                        fieldWithPath("dataList.[].orderNumber").type(JsonFieldType.STRING)
                                .description("주문번호"),
                        fieldWithPath("dataList.[].orderDateTime").type(JsonFieldType.STRING)
                                .description("주문일시"),
                        fieldWithPath("dataList.[].orderCode").type(JsonFieldType.STRING)
                                .description("주문 구분")
                )
        ));
    }

    @Test
    void getOrderSheetData_success() throws Exception {
        //given
        List<ProductOrderSheetResponseDto> orderProducts = new ArrayList<>();

        List<String> titles = List.of("책 1번", "책 2번", "책 3번", "책 4번", "책 5번");
        for (int i = 0; i < 5; i++) {
            String isbn = "12342736-4812204";
            orderProducts.add(new ProductOrderSheetResponseDto((long) i,
                    isbn + i,
                    titles.get(i),
                    10000L,
                    10,
                    true,
                    10,
                    10L
            ));
        }

        //when
        ResultActions result = mockMvc.perform(get("/v1/order-sheets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("productList", objectMapper.writeValueAsString(orderProducts))
                .param("page", "1"));

        //then

        //docs
    }

    @WithMockUser
    @Test
    @DisplayName("주문번호를 통해 주문 상세 정보가 조회 된다.")
    void getOrderDetails() throws Exception {
        // given
        OrderDetailsResponseDto responseDto = new OrderDetailsResponseDto();

        NonMemberOrder nonMemberOrder = DummyOrder.nonMemberOrder();

        OrderResponseDto orderDto = new OrderResponseDto();
        orderDto.setOrderInfoFromNonMemberOrder(nonMemberOrder);
        orderDto.setOrderStatusCode(OrderStatusCode.DEPOSIT);
        responseDto.setOrder(orderDto);

        List<OrderProductResponseDto> productDtos = new ArrayList<>();

        String isbn = "000000000000";
        String url = "https://api-storage.cloud.toast.com/v1/AUTH/container/domain/type";

        File thumbnailFile = DummyFile.dummy(url + "/image.png");
        File ebookFile = DummyFile.dummy(url + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();
        SubscribeProduct subscribeProduct = SubscribeProduct.builder().ISSN("12345").build();

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Product product = DummyProduct.dummy(isbn + i,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            products.add(product);
        }

        int count = 0;
        for (Product product : products) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(nonMemberOrder)
                    .quantity(count++)
                    .isCanceled(false)
                    .build();
            productDtos.add(OrderProductResponseDto.fromEntity(orderProduct, count));
        }
        responseDto.setOrderProducts(productDtos);

        PaymentResponseDto paymentDto = new PaymentResponseDto();
        Payment payment = DummyPayment.payment("paymentId", nonMemberOrder, PaymentCode.EASY_PAY);
        PaymentEasyPay paymentEasyPay = DummyPaymentEasyPay.paymentEasyPay(payment);
        payment.setPaymentEasyPay(paymentEasyPay);
        paymentDto.setEasyPayInfo(payment);
        responseDto.setPayment(paymentDto);

        Mockito.when(queryOrderService.getDetailsDtoByOrderNumber(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/v1/orders/{orderNumber}",
                nonMemberOrder.getOrderNumber()
        ).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.order.orderNumber",
                        equalTo(responseDto.getOrder().getOrderNumber())
                ))
                .andExpect(jsonPath("$.data.order.orderStatusCode",
                        equalTo(responseDto.getOrder().getOrderStatusCode().toString())
                ))
                .andExpect(jsonPath("$.data.orderProducts.[0].productDto.isbn",
                        equalTo(responseDto.getOrderProducts().get(0).getProductDto().getIsbn())
                ))
                .andExpect(jsonPath("$.data.payment.paymentId",
                        equalTo(responseDto.getPayment().getPaymentId())
                ))
                .andExpect(jsonPath("$.data.payment.easyPayProvider",
                        equalTo(responseDto.getPayment().getEasyPayProvider())
                ));

        // docs
        perform.andDo(document(
                "get-order-details",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("orderNumber").description("조회하고자 하는 주문 번호")
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("productsAmount").type(JsonFieldType.NUMBER)
                                .description("총 제품 실 가격").optional(),
                        fieldWithPath("discountsAmount").type(JsonFieldType.NUMBER)
                                .description("총 할인 가격").optional(),
                        fieldWithPath("order.ordererName").type(JsonFieldType.STRING)
                                .description("주문자 이름").optional(),
                        fieldWithPath("order.ordererPhoneNumber").type(JsonFieldType.STRING)
                                .description("주문자 전화번호").optional(),
                        fieldWithPath("order.orderDateTime").type(JsonFieldType.STRING)
                                .description("주문 일시").optional(),
                        fieldWithPath("order.recipientName").type(JsonFieldType.STRING)
                                .description("수령인 이름").optional(),
                        fieldWithPath("order.recipientPhoneNumber").type(JsonFieldType.STRING)
                                .description("수령인 전화번호").optional(),
                        fieldWithPath("order.orderAddress").type(JsonFieldType.STRING)
                                .description("주문 배송지").optional(),
                        fieldWithPath("order.expectedTransportDate").type(JsonFieldType.STRING)
                                .description("희망 배송일자").optional(),
                        fieldWithPath("order.isHidden").type(JsonFieldType.BOOLEAN)
                                .description("주문 숨김 여부").optional(),
                        fieldWithPath("order.orderCode").type(JsonFieldType.STRING)
                                .description("주문 코드").optional(),
                        fieldWithPath("order.orderNumber").type(JsonFieldType.STRING)
                                .description("주문번호").optional(),
                        fieldWithPath("order.orderName").type(JsonFieldType.STRING)
                                .description("주문명").optional(),
                        fieldWithPath("order.usedPoint").type(JsonFieldType.NUMBER)
                                .description("사용 포인트").optional(),
                        fieldWithPath("order.shippingFee").type(JsonFieldType.NUMBER)
                                .description("배송비").optional(),
                        fieldWithPath("order.wrappingFee").type(JsonFieldType.NUMBER)
                                .description("포장비").optional(),
                        fieldWithPath("order.totalAmount").type(JsonFieldType.NUMBER)
                                .description("총 가격").optional(),
                        fieldWithPath("order.orderStatusCode").type(JsonFieldType.STRING)
                                .description("주문상태코드").optional(),
                        fieldWithPath("order.expectedDay").type(JsonFieldType.NUMBER)
                                .description("희망 구독일").optional(),
                        fieldWithPath("order.intervalMonth").type(JsonFieldType.NUMBER)
                                .description("구독 간격").optional(),
                        fieldWithPath("order.nextRenewalDate").type(JsonFieldType.STRING)
                                .description("다음 제공 일자").optional(),

                        fieldWithPath("orderProducts.[].productDto.productId").type(JsonFieldType.NUMBER)
                                .description("주문 상품 id").optional(),
                        fieldWithPath("orderProducts.[].productDto.isbn").type(JsonFieldType.STRING)
                                .description("주문 상품 ISBN").optional(),
                        fieldWithPath("orderProducts.[].productDto.title").type(JsonFieldType.STRING)
                                .description("주문 상품 제목").optional(),
                        fieldWithPath("orderProducts.[].productDto.actualPrice").type(JsonFieldType.NUMBER)
                                .description("주문 상품 실 가격").optional(),
                        fieldWithPath("orderProducts.[].productDto.discountRate").type(JsonFieldType.NUMBER)
                                .description("주문 상품 할인률").optional(),
                        fieldWithPath("orderProducts.[].productDto.isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("주문 상품 포인트 지급 여부").optional(),
                        fieldWithPath("orderProducts.[].productDto.givenPointRate").type(JsonFieldType.NUMBER)
                                .description("주문 상품 제공 포인트").optional(),
                        fieldWithPath("orderProducts.[].productDto.quantity").type(JsonFieldType.NUMBER)
                                .description("상품 개수").optional(),
                        fieldWithPath("orderProducts.[].quantity").type(JsonFieldType.NUMBER)
                                .description("주문 상품 개수").optional(),

                        fieldWithPath("payment.paymentId").type(JsonFieldType.STRING)
                                .description("결제정보 아이디").optional(),
                        fieldWithPath("payment.method").type(JsonFieldType.STRING)
                                .description("결제 방법").optional(),
                        fieldWithPath("payment.currency").type(JsonFieldType.STRING)
                                .description("결제 통화").optional(),
                        fieldWithPath("payment.paymentTotalAmount").type(JsonFieldType.NUMBER)
                                .description("결제 총 금액").optional(),
                        fieldWithPath("payment.approvedDateTime").type(JsonFieldType.STRING)
                                .description("결제 승인 일시").optional(),
                        fieldWithPath("payment.cardCode").type(JsonFieldType.STRING)
                                .description("카드 종류").optional(),
                        fieldWithPath("payment.cardOwnerCode").type(JsonFieldType.STRING)
                                .description("카드 소유 구분").optional(),
                        fieldWithPath("payment.cardNumber").type(JsonFieldType.STRING)
                                .description("카드 번호").optional(),
                        fieldWithPath("payment.cardInstallmentPlanMonths").type(JsonFieldType.NUMBER)
                                .description("할부 개월 수").optional(),
                        fieldWithPath("payment.cardApproveNumber").type(JsonFieldType.STRING)
                                .description("카드 결제 승인 번호").optional(),
                        fieldWithPath("payment.cardAcquirerCode").type(JsonFieldType.STRING)
                                .description("카드 매입사").optional(),
                        fieldWithPath("payment.easyPayProvider").type(JsonFieldType.STRING)
                                .description("간편결제 제공").optional(),
                        fieldWithPath("payment.easyPayAmount").type(JsonFieldType.NUMBER)
                                .description("간편결제 결제금액").optional(),
                        fieldWithPath("payment.easyPayDiscountAmount").type(JsonFieldType.NUMBER)
                                .description("간편결제 즉시 할인 금액").optional()
                )
        ));

    }
}
