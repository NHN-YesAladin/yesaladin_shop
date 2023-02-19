package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import java.util.Collections;
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
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.BestsellerResponseDto;
import shop.yesaladin.shop.order.dto.OrderDetailsResponseDto;
import shop.yesaladin.shop.order.dto.OrderProductResponseDto;
import shop.yesaladin.shop.order.dto.OrderResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.SalesStatisticsResponseDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
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
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

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
                .thenReturn(new PageImpl<>(List.of(new OrderSummaryDto(
                        "123",
                        LocalDateTime.of(2023, 1, 1, 0, 0),
                        OrderCode.MEMBER_ORDER
                ))));

        Map<String, Object> request = Map.of(
                "startDate",
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

        verify(queryOrderService, times(1))
                .getAllOrderListInPeriod(dtoCaptor.capture(), pageableCaptor.capture());
        PeriodQueryRequestDto actualDto = dtoCaptor.getValue();
        Pageable actualPageable = pageableCaptor.getValue();

        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                        PeriodQueryRequestDto.class,
                        "startDate",
                        actualDto
                ).get())
                .isEqualTo(request.get("startDate"));
        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                        PeriodQueryRequestDto.class,
                        "endDate",
                        actualDto
                ).get())
                .isEqualTo(request.get("endDate"));
        Assertions.assertThat(actualPageable.getPageSize()).isEqualTo(20);
        Assertions.assertThat(actualPageable.getPageNumber()).isEqualTo(1);

        // docs
        resultActions.andDo(document(
                "get-all-order-in-period",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                requestFields(
                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("조회 시작 일자")
                                .optional()
                                .attributes(getDateFormat()),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("조회 종료 일자")
                                .optional()
                                .attributes(getDateFormat())
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
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
            orderProducts.add(new ProductOrderSheetResponseDto(
                    (long) i,
                    isbn + i,
                    titles.get(i),
                    10000L,
                    10,
                    true,
                    10,
                    10L,
                    Collections.emptyList()
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
            Product product = DummyProduct.dummy(
                    isbn + i,
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

        Mockito.when(queryOrderService.isMemberOrder(any())).thenReturn(false);
        Mockito.when(queryOrderService.getDetailsDtoByOrderNumber(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/orders/{orderNumber}",
                nonMemberOrder.getOrderNumber()
        ).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath(
                        "$.data.order.orderNumber",
                        equalTo(responseDto.getOrder().getOrderNumber())
                ))
                .andExpect(jsonPath(
                        "$.data.order.orderStatusCode",
                        equalTo(responseDto.getOrder().getOrderStatusCode().toString())
                ))
                .andExpect(jsonPath(
                        "$.data.orderProducts.[0].productDto.isbn",
                        equalTo(responseDto.getOrderProducts().get(0).getProductDto().getIsbn())
                ))
                .andExpect(jsonPath(
                        "$.data.payment.paymentId",
                        equalTo(responseDto.getPayment().getPaymentId())
                ))
                .andExpect(jsonPath(
                        "$.data.payment.easyPayProvider",
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
                requestParameters(
                        parameterWithName("type").description("비회원 조회인지 확인하는 파라미터")
                                .optional()
                                .attributes(defaultValue("none"))
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
                        fieldWithPath("orderProducts.[].productDto.givenPointRate").type(
                                        JsonFieldType.NUMBER)
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

    @WithMockUser
    @Test
    @DisplayName("회원 주문번호를 비회원 주문 조회에서 조회할 경우 예외가 발생한다.")
    void getOrderDetails_byFindingNonMember_fail() throws Exception {
        // given
        Member member = DummyMember.memberWithId();
        MemberAddress memberAddress = DummyMemberAddress.addressWithId(member);
        MemberOrder memberOrder = DummyOrder.memberOrderWithId(
                member,
                memberAddress
        );

        Mockito.when(queryOrderService.isMemberOrder(any())).thenReturn(true);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/orders/{orderNumber}",
                memberOrder.getOrderNumber()
        ).queryParam("type", "none").contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.FORBIDDEN.value())));

        // docs
        perform.andDo(document(
                "get-order-details-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("orderNumber").description("조회하고자 하는 주문 번호")
                ),
                requestParameters(
                        parameterWithName("type").description("비회원 조회인지 확인하는 파라미터")
                                .optional()
                                .attributes(defaultValue("none"))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("데이터 리스트 ")
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("정해진 기간동안의 매출 통계 정보를 조회하여 반환 성공")
    void getSalesStatistics() throws Exception {
        // given
        List<SalesStatisticsResponseDto> dataList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dataList.add(
                    new SalesStatisticsResponseDto(
                            i,
                            "title" + i,
                            i,
                            i,
                            i + "000",
                            i,
                            i,
                            i + "000"
                    )
            );
        }

        PaginatedResponseDto<SalesStatisticsResponseDto> paginated = PaginatedResponseDto.<SalesStatisticsResponseDto>builder()
                .totalPage(2)
                .currentPage(0)
                .totalDataCount(10)
                .dataList(dataList)
                .build();

        Mockito.when(queryOrderService.getSalesStatistics(anyString(), anyString(), any()))
                .thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/orders/statistics")
                .param("start", "2023-02-18")
                .param("end", "2023-02-18")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())));

        verify(queryOrderService, times(1)).getSalesStatistics(anyString(), anyString(), any());

        // docs
        result.andDo(document(
                "get-sales-statistics",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("start").description("시작일"),
                        parameterWithName("end").description("종료일")
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("데이터 개수"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("dataList.[].title").type(JsonFieldType.STRING)
                                .description("제목"),
                        fieldWithPath("dataList.[].numberOfOrders").type(JsonFieldType.NUMBER)
                                .description("주문 건수"),
                        fieldWithPath("dataList.[].totalQuantity").type(JsonFieldType.NUMBER)
                                .description("주문 개수"),
                        fieldWithPath("dataList.[].netSales").type(JsonFieldType.STRING)
                                .description("순매출액"),
                        fieldWithPath("dataList.[].numberOfOrderCancellations").type(JsonFieldType.NUMBER)
                                .description("주문 취소 건수"),
                        fieldWithPath("dataList.[].totalCancelQuantity").type(JsonFieldType.NUMBER)
                                .description("주문 취소 개수"),
                        fieldWithPath("dataList.[].cancelSales").type(JsonFieldType.STRING)
                                .description("주문 취소 금액")
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("지난 1년간의 베스트셀러를 조회하여 반환 성공")
    void getBestseller() throws Exception {
        // given
        List<BestsellerResponseDto> dataList = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            dataList.add(
                    new BestsellerResponseDto(
                            i,
                            "title" + i,
                            "url" + i,
                            List.of(new AuthorsResponseDto(i, "author" + i, null)),
                            new PublisherResponseDto(i, "publisher" + i),
                            i * 1000
                    )
            );
        }

        Mockito.when(queryOrderService.getBestseller()).thenReturn(dataList);

        // when
        ResultActions result = mockMvc.perform(get("/v1/bestseller")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())));

        verify(queryOrderService, times(1)).getBestseller();

        // docs
        result.andDo(document(
                "get-bestseller",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("상품 아이디"),
                        fieldWithPath("title").type(JsonFieldType.STRING)
                                .description("상품 제목"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("상품 썸네일 URL"),
                        fieldWithPath("authors.[].id").type(JsonFieldType.NUMBER)
                                .description("저자 아이디"),
                        fieldWithPath("authors.[].name").type(JsonFieldType.STRING)
                                .description("저자 이름"),
                        fieldWithPath("authors.[].loginId").type(JsonFieldType.STRING)
                                .description("저자 로그인 아이디").optional(),
                        fieldWithPath("publisher.id").type(JsonFieldType.NUMBER)
                                .description("출판사 아이디"),
                        fieldWithPath("publisher.name").type(JsonFieldType.STRING)
                                .description("출판사 이름"),
                        fieldWithPath("sellingPrice").type(JsonFieldType.NUMBER)
                                .description("상품 판매가")
                )
        ));

    }
}
