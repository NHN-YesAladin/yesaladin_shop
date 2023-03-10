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
    @DisplayName("?????? ?????? ?????? ?????? ????????? ????????????.")
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
                        parameterWithName("size").description("?????????????????? ?????????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                requestFields(
                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????")
                                .optional()
                                .attributes(getDateFormat()),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????")
                                .optional()
                                .attributes(getDateFormat())
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ???"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ????????? ??????"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("?????? ???????????? ???"),
                        fieldWithPath("dataList").type(JsonFieldType.ARRAY)
                                .description("????????? ?????? ?????? ????????? ?????????"),
                        fieldWithPath("dataList.[].orderNumber").type(JsonFieldType.STRING)
                                .description("????????????"),
                        fieldWithPath("dataList.[].orderDateTime").type(JsonFieldType.STRING)
                                .description("????????????"),
                        fieldWithPath("dataList.[].orderCode").type(JsonFieldType.STRING)
                                .description("?????? ??????")
                )
        ));
    }

    @Test
    void getOrderSheetData_success() throws Exception {
        //given
        List<ProductOrderSheetResponseDto> orderProducts = new ArrayList<>();

        List<String> titles = List.of("??? 1???", "??? 2???", "??? 3???", "??? 4???", "??? 5???");
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
    @DisplayName("??????????????? ?????? ?????? ?????? ????????? ?????? ??????.")
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
                        parameterWithName("orderNumber").description("??????????????? ?????? ?????? ??????")
                ),
                requestParameters(
                        parameterWithName("type").description("????????? ???????????? ???????????? ????????????")
                                .optional()
                                .attributes(defaultValue("none"))
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("productsAmount").type(JsonFieldType.NUMBER)
                                .description("??? ?????? ??? ??????").optional(),
                        fieldWithPath("discountsAmount").type(JsonFieldType.NUMBER)
                                .description("??? ?????? ??????").optional(),
                        fieldWithPath("order.ordererName").type(JsonFieldType.STRING)
                                .description("????????? ??????").optional(),
                        fieldWithPath("order.ordererPhoneNumber").type(JsonFieldType.STRING)
                                .description("????????? ????????????").optional(),
                        fieldWithPath("order.orderDateTime").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("order.recipientName").type(JsonFieldType.STRING)
                                .description("????????? ??????").optional(),
                        fieldWithPath("order.recipientPhoneNumber").type(JsonFieldType.STRING)
                                .description("????????? ????????????").optional(),
                        fieldWithPath("order.orderAddress").type(JsonFieldType.STRING)
                                .description("?????? ?????????").optional(),
                        fieldWithPath("order.expectedTransportDate").type(JsonFieldType.STRING)
                                .description("?????? ????????????").optional(),
                        fieldWithPath("order.isHidden").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????").optional(),
                        fieldWithPath("order.orderCode").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("order.orderNumber").type(JsonFieldType.STRING)
                                .description("????????????").optional(),
                        fieldWithPath("order.orderName").type(JsonFieldType.STRING)
                                .description("?????????").optional(),
                        fieldWithPath("order.usedPoint").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????").optional(),
                        fieldWithPath("order.shippingFee").type(JsonFieldType.NUMBER)
                                .description("?????????").optional(),
                        fieldWithPath("order.wrappingFee").type(JsonFieldType.NUMBER)
                                .description("?????????").optional(),
                        fieldWithPath("order.totalAmount").type(JsonFieldType.NUMBER)
                                .description("??? ??????").optional(),
                        fieldWithPath("order.orderStatusCode").type(JsonFieldType.STRING)
                                .description("??????????????????").optional(),
                        fieldWithPath("order.expectedDay").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????").optional(),
                        fieldWithPath("order.intervalMonth").type(JsonFieldType.NUMBER)
                                .description("?????? ??????").optional(),
                        fieldWithPath("order.nextRenewalDate").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????").optional(),

                        fieldWithPath("orderProducts.[].productDto.productId").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? id").optional(),
                        fieldWithPath("orderProducts.[].productDto.isbn").type(JsonFieldType.STRING)
                                .description("?????? ?????? ISBN").optional(),
                        fieldWithPath("orderProducts.[].productDto.title").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????").optional(),
                        fieldWithPath("orderProducts.[].productDto.actualPrice").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??? ??????").optional(),
                        fieldWithPath("orderProducts.[].productDto.discountRate").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ?????????").optional(),
                        fieldWithPath("orderProducts.[].productDto.isGivenPoint").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ????????? ?????? ??????").optional(),
                        fieldWithPath("orderProducts.[].productDto.givenPointRate").type(
                                        JsonFieldType.NUMBER)
                                .description("?????? ?????? ?????? ?????????").optional(),
                        fieldWithPath("orderProducts.[].productDto.quantity").type(JsonFieldType.NUMBER)
                                .description("?????? ??????").optional(),
                        fieldWithPath("orderProducts.[].quantity").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??????").optional(),

                        fieldWithPath("payment.paymentId").type(JsonFieldType.STRING)
                                .description("???????????? ?????????").optional(),
                        fieldWithPath("payment.method").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("payment.currency").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("payment.paymentTotalAmount").type(JsonFieldType.NUMBER)
                                .description("?????? ??? ??????").optional(),
                        fieldWithPath("payment.approvedDateTime").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????").optional(),
                        fieldWithPath("payment.cardCode").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("payment.cardOwnerCode").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????").optional(),
                        fieldWithPath("payment.cardNumber").type(JsonFieldType.STRING)
                                .description("?????? ??????").optional(),
                        fieldWithPath("payment.cardInstallmentPlanMonths").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ???").optional(),
                        fieldWithPath("payment.cardApproveNumber").type(JsonFieldType.STRING)
                                .description("?????? ?????? ?????? ??????").optional(),
                        fieldWithPath("payment.cardAcquirerCode").type(JsonFieldType.STRING)
                                .description("?????? ?????????").optional(),
                        fieldWithPath("payment.easyPayProvider").type(JsonFieldType.STRING)
                                .description("???????????? ??????").optional(),
                        fieldWithPath("payment.easyPayAmount").type(JsonFieldType.NUMBER)
                                .description("???????????? ????????????").optional(),
                        fieldWithPath("payment.easyPayDiscountAmount").type(JsonFieldType.NUMBER)
                                .description("???????????? ?????? ?????? ??????").optional()
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("?????? ??????????????? ????????? ?????? ???????????? ????????? ?????? ????????? ????????????.")
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
                        parameterWithName("orderNumber").description("??????????????? ?????? ?????? ??????")
                ),
                requestParameters(
                        parameterWithName("type").description("????????? ???????????? ???????????? ????????????")
                                .optional()
                                .attributes(defaultValue("none"))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("????????? ????????? ")
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("????????? ??????????????? ?????? ?????? ????????? ???????????? ?????? ??????")
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
                        parameterWithName("start").description("?????????"),
                        parameterWithName("end").description("?????????")
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("dataList.[].title").type(JsonFieldType.STRING)
                                .description("??????"),
                        fieldWithPath("dataList.[].numberOfOrders").type(JsonFieldType.NUMBER)
                                .description("?????? ??????"),
                        fieldWithPath("dataList.[].totalQuantity").type(JsonFieldType.NUMBER)
                                .description("?????? ??????"),
                        fieldWithPath("dataList.[].netSales").type(JsonFieldType.STRING)
                                .description("????????????"),
                        fieldWithPath("dataList.[].numberOfOrderCancellations").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("dataList.[].totalCancelQuantity").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("dataList.[].cancelSales").type(JsonFieldType.STRING)
                                .description("?????? ?????? ??????")
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("?????? 1????????? ?????????????????? ???????????? ?????? ??????")
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
                                .description("?????? ?????????"),
                        fieldWithPath("title").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("thumbnailFileUrl").type(JsonFieldType.STRING)
                                .description("?????? ????????? URL"),
                        fieldWithPath("authors.[].id").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("authors.[].name").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("authors.[].loginId").type(JsonFieldType.STRING)
                                .description("?????? ????????? ?????????").optional(),
                        fieldWithPath("publisher.id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("publisher.name").type(JsonFieldType.STRING)
                                .description("????????? ??????"),
                        fieldWithPath("sellingPrice").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????")
                )
        ));

    }
}
