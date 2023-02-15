package shop.yesaladin.shop.coupon.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.common.aspect.advice.LoginIdAspect;
import shop.yesaladin.shop.coupon.dto.CouponUseRequestDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.UseCouponService;

@Import({AopAutoConfiguration.class, LoginIdAspect.class})
@WebMvcTest(UseMemberCouponController.class)
@AutoConfigureRestDocs
class UseMemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UseCouponService useCouponService;
    @Autowired
    private ObjectMapper objectMapper;
    private List<String> couponCodes;

    @BeforeEach
    void setUp() {
        couponCodes = List.of(UUID.randomUUID().toString());
    }

    @Test
    @DisplayName("포인트 쿠폰 사용 요청 성공")
    @WithMockUser("mongmeo")
    void pointCouponUseRequestMessageTest() throws Exception {
        // given
        CouponUseRequestDto requestBody = new CouponUseRequestDto(couponCodes);
        String requestId = UUID.randomUUID().toString();

        Mockito.when(useCouponService.sendCouponUseRequest(Mockito.anyString(), Mockito.anyList()))
                .thenReturn(new RequestIdOnlyDto(requestId));

        // when
        ResultActions actions = mockMvc.perform(post("/v1/member-coupons/usage").contentType(
                        MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.requestId", equalTo(requestId)));

        // docs
        actions.andDo(document(
                "use-member-coupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("couponCodes").type(JsonFieldType.ARRAY)
                                .description("사용할 쿠폰 코드")
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("requestId").type(JsonFieldType.STRING)
                                .description("쿠폰 사용 요청 메시지의 requestId")
                )
        ));
    }
}