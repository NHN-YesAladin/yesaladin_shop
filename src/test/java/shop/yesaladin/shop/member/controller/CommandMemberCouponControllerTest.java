package shop.yesaladin.shop.member.controller;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@AutoConfigureRestDocs
@WebMvcTest(CommandMemberCouponController.class)
class CommandMemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandMemberCouponService commandMemberCouponService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @WithMockUser
    @Test
    void test() throws Exception {
        // given
        String couponCode = "e37ca01a-0fbb-4587-9972-97ccb971a815";
        String couponCode2 = "a265b062-a352-11ed-a8fc-0242ac120002";

        Map<String, Object> memberCouponRequestDto = new HashMap<>();
        memberCouponRequestDto.put("memberDto", MemberDto.fromEntity(MemberDummy.dummy()));
        memberCouponRequestDto.put("couponCodes", List.of(couponCode, couponCode2));
        memberCouponRequestDto.put(
                "couponGroupCodes",
                List.of(
                        "c1b09f26-fd02-489c-bff3-6e460e2dbf14",
                        "344f10d4-9fb0-429f-b813-20e95ef62ca5"
                )
        );

        // when
        Mockito.when(commandMemberCouponService.createMemberCoupons(Mockito.any()))
                .thenReturn(new MemberCouponResponseDto(List.of(couponCode, couponCode2)));
        ResultActions result = mockMvc.perform(post("/v1/coupons")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(memberCouponRequestDto))));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.givenCouponCodeList").isArray())
                .andExpect(jsonPath("$.data.givenCouponCodeList.[0]", equalTo(couponCode)));

        // docs
        result.andDo(
                document(
                        "register-member-coupons-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("[].couponCodes").type(JsonFieldType.ARRAY)
                                        .description("등록할 쿠폰 코드 리스트"),
                                fieldWithPath("[].couponGroupCodes").type(JsonFieldType.ARRAY)
                                        .description("등록할 쿠폰의 그룹 코드 리스트"),
                                fieldWithPath("[].memberDto").type(JsonFieldType.OBJECT)
                                        .description("쿠폰을 등록할 회원")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("givenCouponCodeList").type(JsonFieldType.ARRAY)
                                        .description("지급 완료된 쿠폰 코드 리스트")
                        )
                )
        );

    }

}