package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;

@Disabled
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("local-test")
public class CommandOrderControllerSecurityTest {

    MockMvc mockMvc;

    @MockBean
    CommandOrderService commandOrderService;

    @Autowired
    ObjectMapper objectMapper;

    String ordererName = "김몽머";
    String ordererPhoneNumber = "01012341234";
    String ordererAddress = "서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)";
    LocalDate expectedShippingDate = LocalDate.of(2023, 1, 5);
    String recipientName = "김몽대";
    String recipientPhoneNumber = "01029482743";
    List<ProductOrderRequestDto> orderProducts;
    List<ProductOrderRequestDto> subscribeOrderProducts;
    long nonMemberProductTotalAmount = 10000L;
    long productTotalAmount = 9000L;
    int shippingFee = 3000;
    int wrappingFee = 0;
    Long ordererAddressId = 1L;
    List<String> orderCoupons;
    long usePoint = 1000L;
    long savePoint = 500L;
    int expectedDay = 10;
    int intervalMonth = 6;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommandOrderController(commandOrderService))
                .build();

        orderProducts = new ArrayList<>();
        subscribeOrderProducts = new ArrayList<>();

        String isbn = "152374859182";
        for (int i = 0; i < 5; i++) {
            int quantity = i + 1;
            orderProducts.add(new ProductOrderRequestDto(isbn + i, quantity));
        }
        subscribeOrderProducts.add(new ProductOrderRequestDto(isbn + 7, 2));
    }

    @Test
    @WithMockUser(username = "user@1", authorities = "ROLE_USER")
    @DisplayName("비회원 주문 실패 - [권한] 비회원이 아닌 경우")
    void createNonMemberOrders_fail_unauthorized() throws Exception {
        //given
        OrderNonMemberCreateRequestDto request = getNonMemberOrderRequest();

        //when
        ResultActions result = mockMvc.perform(post("/v1/orders/non-member")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.UNAUTHORIZED.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "create-non-member-order-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("expectedShippingDate").type(JsonFieldType.STRING)
                                .description("희망 배송 일자")
                                .optional(),
                        fieldWithPath("orderProducts").type(JsonFieldType.ARRAY)
                                .description("주문 상품 목록"),
                        fieldWithPath("orderProducts.[].isbn").type(JsonFieldType.STRING)
                                .description("주문 상품"),
                        fieldWithPath("orderProducts.[].quantity").type(JsonFieldType.NUMBER)
                                .description("주문 상품 수량"),
                        fieldWithPath("productTotalAmount").type(JsonFieldType.NUMBER)
                                .description("총 상품 금액"),
                        fieldWithPath("shippingFee").type(JsonFieldType.NUMBER).description("배송비"),
                        fieldWithPath("wrappingFee").type(JsonFieldType.NUMBER).description("포장비"),
                        fieldWithPath("recipientName").type(JsonFieldType.STRING)
                                .description("수령인명"),
                        fieldWithPath("recipientPhoneNumber").type(JsonFieldType.STRING)
                                .description("수령인 연락처"),
                        fieldWithPath("ordererName").type(JsonFieldType.STRING).description("주문자명"),
                        fieldWithPath("ordererPhoneNumber").type(JsonFieldType.STRING)
                                .description("주문자 연락처"),
                        fieldWithPath("ordererAddress").type(JsonFieldType.STRING)
                                .description("주문자 주소")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

    @Test
    @WithMockUser(username = "anonymous", authorities = "ROLE_ANONYMOUS")
    @DisplayName("회원 주문 실패 - [권한] 인증이 안된 경우")
    void createMemberOrders_fail_unauthorized() throws Exception {
        //given
        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        //when
        ResultActions result = mockMvc.perform(post("/v1/orders/member")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.UNAUTHORIZED.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "create-member-order-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("expectedShippingDate").type(JsonFieldType.STRING)
                                .description("희망 배송 일자")
                                .optional(),
                        fieldWithPath("orderProducts").type(JsonFieldType.ARRAY)
                                .description("주문 상품 목록"),
                        fieldWithPath("orderProducts.[].isbn").type(JsonFieldType.STRING)
                                .description("주문 상품"),
                        fieldWithPath("orderProducts.[].quantity").type(JsonFieldType.NUMBER)
                                .description("주문 상품 수량"),
                        fieldWithPath("productTotalAmount").type(JsonFieldType.NUMBER)
                                .description("총 상품 금액"),
                        fieldWithPath("shippingFee").type(JsonFieldType.NUMBER).description("배송비"),
                        fieldWithPath("wrappingFee").type(JsonFieldType.NUMBER).description("포장비"),
                        fieldWithPath("recipientName").type(JsonFieldType.STRING)
                                .description("수령인명"),
                        fieldWithPath("recipientPhoneNumber").type(JsonFieldType.STRING)
                                .description("수령인 연락처"),
                        fieldWithPath("ordererAddressId").type(JsonFieldType.NUMBER)
                                .description("회원 배송지 Pk"),
                        fieldWithPath("orderCoupons").type(JsonFieldType.ARRAY)
                                .description("주문에 사용한 쿠폰")
                                .optional(),
                        fieldWithPath("orderPoint").type(JsonFieldType.NUMBER)
                                .description("주문에 사용한 포인트")

                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ANONYMOUS")
    @DisplayName("정기구독 주문 실패 - [권한] 인증이 안된 경우")
    void createSubscribeOrders_fail_unauthorized() throws Exception {
        //given
        OrderMemberCreateRequestDto request = getSubscribeRequest();

        //when
        ResultActions result = mockMvc.perform(post("/v1/orders/subscribe")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.UNAUTHORIZED.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "create-subscribe-order-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("expectedShippingDate").type(JsonFieldType.STRING)
                                .description("희망 배송 일자")
                                .optional(),
                        fieldWithPath("orderProducts").type(JsonFieldType.ARRAY)
                                .description("주문 상품 목록"),
                        fieldWithPath("orderProducts.[].isbn").type(JsonFieldType.STRING)
                                .description("주문 상품"),
                        fieldWithPath("orderProducts.[].quantity").type(JsonFieldType.NUMBER)
                                .description("주문 상품 수량"),
                        fieldWithPath("productTotalAmount").type(JsonFieldType.NUMBER)
                                .description("총 상품 금액"),
                        fieldWithPath("shippingFee").type(JsonFieldType.NUMBER).description("배송비"),
                        fieldWithPath("wrappingFee").type(JsonFieldType.NUMBER).description("포장비"),
                        fieldWithPath("recipientName").type(JsonFieldType.STRING)
                                .description("수령인명"),
                        fieldWithPath("recipientPhoneNumber").type(JsonFieldType.STRING)
                                .description("수령인 연락처"),
                        fieldWithPath("ordererAddressId").type(JsonFieldType.NUMBER)
                                .description("회원 배송지 Pk"),
                        fieldWithPath("orderCoupons").type(JsonFieldType.ARRAY)
                                .description("주문에 사용한 쿠폰")
                                .optional(),
                        fieldWithPath("orderPoint").type(JsonFieldType.NUMBER)
                                .description("주문에 사용한 포인트"),
                        fieldWithPath("expectedDay").type(JsonFieldType.NUMBER)
                                .description("희망 정기 배송 일자"),
                        fieldWithPath("intervalMonth").type(JsonFieldType.NUMBER)
                                .description("구독 기간")

                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

    private OrderNonMemberCreateRequestDto getNonMemberOrderRequest() {
        return new OrderNonMemberCreateRequestDto(
                expectedShippingDate,
                orderProducts,
                nonMemberProductTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererName,
                ordererPhoneNumber,
                ordererAddress
        );
    }

    private OrderMemberCreateRequestDto getMemberOrderRequest() {
        return new OrderMemberCreateRequestDto(
                expectedShippingDate,
                orderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererAddressId,
                orderCoupons,
                usePoint,
                savePoint
        );
    }

    private OrderSubscribeCreateRequestDto getSubscribeRequest() {
        return new OrderSubscribeCreateRequestDto(
                expectedShippingDate,
                subscribeOrderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererAddressId,
                orderCoupons,
                usePoint,
                savePoint,
                expectedDay,
                intervalMonth
        );
    }
}
