package shop.yesaladin.shop.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.shop.common.aspect.advice.LoginIdAspect;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;

@Import({AopAutoConfiguration.class, LoginIdAspect.class})
@WebMvcTest(GiveMemberCouponController.class)
@ExtendWith(RestDocumentationExtension.class)
class GiveMemberCouponControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private GiveCouponService giveCouponService;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("쿠폰 발행 요청 메시지를 발송한다.")
    @WithMockUser("mongmeo")
    void sendCouponGiveRequestTest() throws Exception {
        // when
        ResultActions actual = mockMvc.perform(post("/v1/member-coupons").contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"triggerTypeCode\": \"MEMBER_GRADE_BRONZE\", \"couponId\": 1, \"requestDateTime\":  \"2023-02-14T17:16:38.858927\"}"));

        // then
        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorMessages").isEmpty());
        Mockito.verify(giveCouponService, Mockito.times(1))
                .requestGiveCoupon("mongmeo", TriggerTypeCode.MEMBER_GRADE_BRONZE, 1L,
                        LocalDateTime.parse("2023-02-14T17:16:38.858927")
                );

        // docs
        actual.andDo(document(
                "send-coupon-give-message",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("triggerTypeCode").type(JsonFieldType.STRING)
                                .description("발행을 요청할 쿠폰의 트리거 타입 코드"),
                        fieldWithPath("couponId").type(JsonFieldType.NUMBER)
                                .description("발행을 요청할 쿠폰 ID")
                                .optional(),
                        fieldWithPath("requestDateTime").type(JsonFieldType.STRING)
                                .description("발행을 요청한 시간")
                )
        ));
    }
}