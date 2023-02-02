package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.getDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
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
    @WithMockUser
    @Test
    @DisplayName("회원 주문에 대해 기간별로 주문을 조회함")
    void getAllOrdersByMemberId() throws Exception {
        // given
        long memberId = 1L;
        int i = 10;
        Mockito.when(queryOrderService.getOrderListInPeriodByMemberId(any(), eq(memberId), any()))
                .thenReturn(new PageImpl<>(List.of(new OrderSummaryResponseDto(
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
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);

        // when
        Integer pageSize = 20;
        Integer page = 0;
        ResultActions result = mockMvc.perform(get("/v1/member-orders")
                .with(csrf())
                .param("startDate", request.get("startDate").toString())
                .param("endDate", request.get("endDate").toString())
                .param("size", pageSize.toString())
                .param("page", page.toString()));

        // then
        ResultActions resultActions = result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPage", equalTo(page)))
                .andExpect(jsonPath("$.dataList.[0].orderId", equalTo(i)))
                .andExpect(jsonPath("$.dataList.[0].orderProductCount", equalTo(i)));


        Mockito.verify(queryOrderService, Mockito.times(1))
                .getOrderListInPeriodByMemberId(
                        dtoCaptor.capture(),
                        longCaptor.capture(),
                        pageableCaptor.capture()
                );
        PeriodQueryRequestDto actualDto = dtoCaptor.getValue();
        Pageable actualPageable = pageableCaptor.getValue();
        Long actualLong = longCaptor.getValue();

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
        Assertions.assertThat(actualLong).isEqualTo(memberId);

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
                        parameterWithName("startDate")
                                .description("조회 시작 일자")
                                .attributes(getDateFormat()),
                        parameterWithName("endDate")
                                .description("조회 종료 일자")
                                .attributes(getDateFormat()),
                        parameterWithName("_csrf").description("csrf")
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
                        fieldWithPath("dataList.[].orderId").type(JsonFieldType.NUMBER)
                                .description("주문 아이디"),
                        fieldWithPath("dataList.[].orderNumber").type(JsonFieldType.STRING)
                                .description("주문 번호"),
                        fieldWithPath("dataList.[].orderDateTime").type(JsonFieldType.STRING)
                                .description("주문 일시"),
                        fieldWithPath("dataList.[].orderName").type(JsonFieldType.STRING)
                                .description("주문 이름"),
                        fieldWithPath("dataList.[].orderAmount").type(JsonFieldType.NUMBER)
                                .description("주문 총 금액"),
                        fieldWithPath("dataList.[].orderStatusCode").type(JsonFieldType.STRING)
                                .description("주문 상태"),
                        fieldWithPath("dataList.[].memberId").type(JsonFieldType.NUMBER)
                                .description("주문자 아이디"),
                        fieldWithPath("dataList.[].memberName").type(JsonFieldType.STRING)
                                .description("주문자 이름"),
                        fieldWithPath("dataList.[].orderProductCount").type(JsonFieldType.NUMBER)
                                .description("주문 상품 종류 개수"),
                        fieldWithPath("dataList.[].productTotalCount").type(JsonFieldType.NUMBER)
                                .description("주문 상품 총 개수"),
                        fieldWithPath("dataList.[].orderCode").type(JsonFieldType.STRING)
                                .description("주문 구분")
                )
        ));
    }
}
