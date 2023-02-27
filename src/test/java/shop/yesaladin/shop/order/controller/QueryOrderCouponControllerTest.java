package shop.yesaladin.shop.order.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderCouponService;

@AutoConfigureRestDocs
@WebMvcTest(QueryOrderCouponController.class)
class QueryOrderCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryOrderCouponService queryOrderCouponService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user@1")
    void calculateCoupon() throws Exception {
        //given
        String isbn = "0000000000001";
        int quantity = 2;
        String couponCode = UUID.randomUUID().toString();
        List<String> duplicateCouponCode = List.of(UUID.randomUUID().toString());

        CouponOrderSheetResponseDto response = new CouponOrderSheetResponseDto(
                isbn,
                List.of(couponCode),
                List.of("1000원 할인 쿠폰"),
                1000,
                1000
        );
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("isbn", isbn);
        params.add("quantity", quantity + "");
        params.add("couponCode", couponCode);
        params.add("duplicateCouponCode", String.join(",", duplicateCouponCode));

        Mockito.when(queryOrderCouponService.calculateCoupons(any(), any()))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/order-coupons")
                .params(params)
                .with(csrf()));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn", equalTo(isbn)))
                .andExpect(jsonPath("$.memberCoupons[0]", equalTo(couponCode)))
                .andExpect(jsonPath("$.memberCouponNames[0]", equalTo("1000원 할인 쿠폰")))
                .andExpect(jsonPath("$.discountPrice", equalTo(1000)))
                .andExpect(jsonPath("$.expectedPoint", equalTo(1000)));

        //docs
        result.andDo(document(
                "calculateCoupon-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("_csrf").description("csrf"),
                        parameterWithName("isbn").description("상품의 isbn"),
                        parameterWithName("quantity").description("쿠폰 적용 할인 금액"),
                        parameterWithName("couponCode").description("일반 쿠폰 코드"),
                        parameterWithName("duplicateCouponCode").description("중복 쿠폰 코드 목록")
                ),
                responseFields(
                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("상품의 isbn"),
                        fieldWithPath("memberCoupons").type(JsonFieldType.ARRAY)
                                .description("쿠폰 코드 목록"),
                        fieldWithPath("memberCouponNames").type(JsonFieldType.ARRAY)
                                .description("쿠폰명 목록"),
                        fieldWithPath("discountPrice").type(JsonFieldType.NUMBER)
                                .description("쿠폰 적용 할인 금액"),
                        fieldWithPath("expectedPoint").type(JsonFieldType.NUMBER)
                                .description("적립 예상 포인트 금액")
                )
        ));
    }
}