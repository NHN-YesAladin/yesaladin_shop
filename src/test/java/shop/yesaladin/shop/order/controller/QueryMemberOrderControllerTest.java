package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.getDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

@AutoConfigureRestDocs
@WebMvcTest(QueryMemberOrderController.class)
class QueryMemberOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryOrderService queryOrderService;

    @BeforeEach
    void setUp() {
    }

    @WithMockUser(username = "loginId")
    @Test
    @DisplayName("?????? ????????? ?????? ???????????? ????????? ?????????")
    void getAllOrdersByMemberId() throws Exception {
        // given
        int i = 10;
        PageImpl<OrderSummaryResponseDto> dtoPage = new PageImpl<>(List.of(new OrderSummaryResponseDto(
                (long) i,
                "orderNumber" + i,
                LocalDateTime.now().minusDays(5),
                "name",
                (long) 10000 * i,
                OrderStatusCode.ORDER,
                (long) i,
                "memberName",
                (long) i,
                i,
                OrderCode.MEMBER_ORDER
        )));
        Mockito.when(queryOrderService.getOrderListInPeriodByMemberId(any(), any(), any()))
                .thenReturn(dtoPage);

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
        Integer pageSize = 20;
        Integer page = 0;
        ResultActions result = mockMvc.perform(get("/v1/member-orders")
//                .with(csrf())
                .param("startDate", request.get("startDate").toString())
                .param("endDate", request.get("endDate").toString())
                .param("size", pageSize.toString())
                .param("page", page.toString()));

        // then
        ResultActions resultActions = result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.currentPage", equalTo(page)))
                .andExpect(jsonPath("$.data.dataList.[0].orderId", equalTo(i)))
                .andExpect(jsonPath("$.data.dataList.[0].orderProductCount", equalTo(i)));

        Mockito.verify(queryOrderService, Mockito.times(1))
                .getOrderListInPeriodByMemberId(
                        dtoCaptor.capture(),
                        any(),
                        pageableCaptor.capture()
                );
        PeriodQueryRequestDto actualDto = dtoCaptor.getValue();
        Pageable actualPageable = pageableCaptor.getValue();

        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "startDate",
                actualDto
        ).get()).isEqualTo(request.get("startDate"));
        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "endDate",
                actualDto
        ).get()).isEqualTo(request.get("endDate"));
        Assertions.assertThat(actualPageable.getPageSize()).isEqualTo(20);
        Assertions.assertThat(actualPageable.getPageNumber()).isEqualTo(page);

        // docs
        resultActions.andDo(document(
                "get-all-order-in-period-by-member",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("?????????????????? ?????????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("startDate")
                                .description("?????? ?????? ??????")
                                .attributes(getDateFormat()),
                        parameterWithName("endDate")
                                .description("?????? ?????? ??????")
                                .attributes(getDateFormat())
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ???"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("?????? ???????????? ???"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY)
                                .description("????????? ?????? ?????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].orderId").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.dataList.[].orderNumber").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].orderDateTime").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].orderName").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].orderAmount").type(JsonFieldType.NUMBER)
                                .description("?????? ??? ??????"),
                        fieldWithPath("data.dataList.[].orderStatusCode").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].memberId").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("data.dataList.[].memberName").type(JsonFieldType.STRING)
                                .description("????????? ??????"),
                        fieldWithPath("data.dataList.[].orderProductCount").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ?????? ??????"),
                        fieldWithPath("data.dataList.[].productTotalCount").type(JsonFieldType.NUMBER)
                                .description("?????? ?????? ??? ??????"),
                        fieldWithPath("data.dataList.[].orderCode").type(JsonFieldType.STRING)
                                .description("?????? ??????")
                )
        ));
    }

    @WithMockUser(username = "loginId")
    @Test
    @DisplayName("?????? ????????? ?????? ?????? ????????? ?????????")
    void getOrdersByStatusAndLoginId() throws Exception {
        // given
        String loginId = "loginId";
        List<OrderStatusResponseDto> responseList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderStatusResponseDto responseDto = OrderStatusResponseDto.builder()
                    .orderId((long) i)
                    .totalAmount((long) (10000 * i))
                    .orderName("orderName" + i)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .orderNumber("number" + i)
                    .receiverName("?????????" + i)
                    .orderDateTime(LocalDateTime.now().plusHours(i))
                    .loginId("loginId" + i)
                    .build();
            responseList.add(responseDto);
        }

        Integer pageSize = 3;
        Integer pageNum = 1;
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        int pagingStart = pageNum * pageSize;
        PageImpl<OrderStatusResponseDto> page = new PageImpl<>(
                responseList.subList(pagingStart, pagingStart + pageSize),
                pageRequest,
                responseList.size()
        );

        Mockito.when(queryOrderService.getStatusResponsesByLoginIdAndStatus(any(), any(), any()))
                .thenReturn(page);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<OrderStatusCode> codeCaptor = ArgumentCaptor.forClass(OrderStatusCode.class);

        // when
        ResultActions result = mockMvc.perform(get("/v1/member-orders")
                .param("status", String.valueOf(1))
                .param("size", pageSize.toString())
                .param("page", pageNum.toString()));

        // then
        ResultActions resultActions = result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.currentPage", equalTo(pageNum)))
                .andExpect(jsonPath(
                        "$.data.dataList.[0].orderId",
                        equalTo(responseList.get(pagingStart).getOrderId().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList.[0].receiverName",
                        equalTo(responseList.get(pagingStart).getReceiverName())
                ))
                .andExpect(jsonPath(
                        "$.data.dataList.[0].totalAmount",
                        equalTo(responseList.get(pagingStart).getTotalAmount().intValue())
                ));

        Mockito.verify(queryOrderService, Mockito.times(1))
                .getStatusResponsesByLoginIdAndStatus(
                        any(),
                        codeCaptor.capture(),
                        pageableCaptor.capture()
                );

        Assertions.assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(codeCaptor.getValue()).isEqualTo(OrderStatusCode.ORDER);

        // docs
        resultActions.andDo(document(
                "get-orders-by-status",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("?????????????????? ?????????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("status")
                                .description("?????? ??????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ???"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("?????? ???????????? ???"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY)
                                .description("????????? ?????? ?????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].orderId").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.dataList.[].orderNumber").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].orderDateTime").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].orderName").type(JsonFieldType.STRING)
                                .description("?????? ??????"),
                        fieldWithPath("data.dataList.[].totalAmount").type(JsonFieldType.NUMBER)
                                .description("?????? ??? ??????"),
                        fieldWithPath("data.dataList.[].loginId").type(JsonFieldType.STRING)
                                .description("????????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].receiverName").type(JsonFieldType.STRING)
                                .description("???????????? ????????? ????????? ??????"),
                        fieldWithPath("data.dataList.[].orderCode").type(JsonFieldType.STRING)
                                .description("?????? ??????")
                )
        ));
    }

    @WithMockUser(username = "loginId")
    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????? ??????")
    void getOrderCountsByStatusAndLoginId() throws Exception {
        // given
        Map<OrderStatusCode, Long> map = new HashMap<>();
        for (OrderStatusCode code : OrderStatusCode.values()) {
            if (code.equals(OrderStatusCode.DEPOSIT) || code.equals(OrderStatusCode.CONFIRM)
                    || code.equals(OrderStatusCode.REFUND)
                    || code.equals(OrderStatusCode.CANCEL)) {

                continue;
            }
            map.put(code, 4L);
        }

        Mockito.when(queryOrderService.getOrderCountByLoginIdStatus(any())).thenReturn(map);

        // when
        ResultActions result = mockMvc.perform(get("/v1/member-orders")
                .param("status-count", ""));

        // then
        ResultActions resultActions = result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.DELIVERY", equalTo(4)))
                .andExpect(jsonPath("$.data.COMPLETE", equalTo(4)));

        Mockito.verify(queryOrderService, Mockito.times(1)).getOrderCountByLoginIdStatus(any());

        // docs
        resultActions.andDo(document(
                "get-order-count-by-status",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.ORDER").type(JsonFieldType.NUMBER)
                                .description("???????????? ????????? ?????? ??????"),
                        fieldWithPath("data.DELIVERY").type(JsonFieldType.NUMBER)
                                .description("????????? ????????? ?????? ??????"),
                        fieldWithPath("data.READY").type(JsonFieldType.NUMBER)
                                .description("???????????? ????????? ?????? ??????"),
                        fieldWithPath("data.COMPLETE").type(JsonFieldType.NUMBER)
                                .description("???????????? ????????? ?????? ??????")
                )
        ));
    }
}
