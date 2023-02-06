package shop.yesaladin.shop.coupon.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
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

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.shop.common.aspect.advice.LoginIdAspect;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;

@Import({AopAutoConfiguration.class, LoginIdAspect.class})
@WebMvcTest(QueryMemberCouponController.class)
@AutoConfigureRestDocs
class QueryMemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryMemberCouponService queryMemberCouponService;

    @Test
    @WithMockUser(username = "loginId")
    @DisplayName("멤버 쿠폰 목록 조회에 성공한다.")
    void getMemberCouponList() throws Exception {
        // given
        List<MemberCouponSummaryDto> result = List.of(new MemberCouponSummaryDto(
                "test coupon",
                "coupon-code",
                1000,
                CouponTypeCode.FIXED_PRICE,
                LocalDate.of(2023, 1, 1),
                false,
                "",
                CouponBoundCode.ALL
        ));
        Mockito.when(queryMemberCouponService.getMemberCouponSummaryList(
                        Mockito.any(),
                        Mockito.anyString()
                ))
                .thenReturn(PaginatedResponseDto.<MemberCouponSummaryDto>builder()
                        .dataList(result)
                        .totalDataCount(1L)
                        .totalPage(1L)
                        .currentPage(1L)
                        .build());

        // when
        ResultActions actual = mockMvc.perform(get("/v1/member-coupons").queryParam("page", "0")
                .queryParam("size", "20"));

        // then
        Mockito.verify(queryMemberCouponService, Mockito.times(1))
                .getMemberCouponSummaryList(Mockito.argThat(arg -> arg.getPageSize() == 20
                        && arg.getPageNumber() == 0), Mockito.eq("loginId"));
        actual.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.totalPage").value(1L))
                .andExpect(jsonPath("$.data.currentPage").value(1L))
                .andExpect(jsonPath("$.data.totalDataCount").value(1L))
                .andExpect(jsonPath("$.data.dataList").isArray());

        // docs
        actual.andDo(document(
                "get-member-coupon-summary-list",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue("0")),
                        parameterWithName("size").description("페이지 사이즈")
                                .optional()
                                .attributes(defaultValue("20"))
                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지 번호"),
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("총 페이지 수"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("총 데이터 수"),
                        fieldWithPath("dataList.[].name").type(JsonFieldType.STRING)
                                .description("쿠폰 이름"),
                        fieldWithPath("dataList.[].couponCode").type(JsonFieldType.STRING)
                                .description("쿠폰 코드"),
                        fieldWithPath("dataList.[].amount").type(JsonFieldType.NUMBER)
                                .description("쿠폰 할인/포인트 값"),
                        fieldWithPath("dataList.[].couponTypeCode").type(JsonFieldType.STRING)
                                .description("쿠폰 유형 코드"),
                        fieldWithPath("dataList.[].expireDate").type(JsonFieldType.STRING)
                                .description("쿠폰 만료일"),
                        fieldWithPath("dataList.[].isUsed").type(JsonFieldType.BOOLEAN)
                                .description("쿠폰 사용 여부"),
                        fieldWithPath("dataList.[].couponBound").type(JsonFieldType.STRING)
                                .description("쿠폰 사용 가능 범위(카테고리 ID / ISBN / 빈값)"),
                        fieldWithPath("dataList.[].couponBoundCode").type(JsonFieldType.STRING)
                                .description("쿠폰 사용 가능 범위 코드")
                )
        ));

    }
}