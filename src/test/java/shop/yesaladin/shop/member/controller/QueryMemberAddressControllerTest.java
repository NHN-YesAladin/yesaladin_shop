package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

@AutoConfigureRestDocs
@WebMvcTest(QueryMemberAddressController.class)
class QueryMemberAddressControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryMemberAddressService queryMemberAddressService;

    @Autowired
    ObjectMapper objectMapper;

    String address = "Gwang Ju";
    Boolean isDefault = false;
    Member member = MemberDummy.dummyWithId(1L);

    @Test
    void getMemberAddressByMemberId_fail_MemberNotFound() throws Exception {
        //given
        long memberId = 1L;

        Mockito.when(queryMemberAddressService.findByMemberId(memberId))
                .thenThrow(new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/{memberId}/addresses", memberId));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        //docs
        result.andDo(document(
                "get-member-address-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void getMemberAddressByMemberId() throws Exception {
        //given
        long memberId = 1L;

        Mockito.when(queryMemberAddressService.findByMemberId(memberId))
                .thenReturn(getMemberAddressList(10));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/{memberId}/addresses", memberId));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].member.id", equalTo((int) memberId)));

        //docs
        result.andDo(document(
                "get-member-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("등록된 배송지 Pk"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING)
                                .description("등록된 배송지 주소"),
                        fieldWithPath("[].default").type(JsonFieldType.BOOLEAN)
                                .description("등록된 배송지의 대표주소 여부"),
                        fieldWithPath("[].member.id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("[].member.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("[].member.name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("[].member.loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("[].member.password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("[].member.birthYear").type(JsonFieldType.NUMBER)
                                .description("회원의 생년"),
                        fieldWithPath("[].member.birthMonth").type(JsonFieldType.NUMBER)
                                .description("회원의 생월"),
                        fieldWithPath("[].member.birthDay").type(JsonFieldType.NUMBER)
                                .description("회원의 생일"),
                        fieldWithPath("[].member.email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("[].member.phone").type(JsonFieldType.STRING)
                                .description("회원의 핸드폰번호"),
                        fieldWithPath("[].member.signUpDate").type(JsonFieldType.STRING)
                                .description("회원의 가입일"),
                        fieldWithPath("[].member.withdrawalDate").type(JsonFieldType.STRING)
                                .description("회원의 탈퇴일").optional(),
                        fieldWithPath("[].member.withdrawal").type(JsonFieldType.BOOLEAN)
                                .description("회원의 탈퇴여부"),
                        fieldWithPath("[].member.blocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단여부"),
                        fieldWithPath("[].member.memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("[].member.memberGenderCode").type(JsonFieldType.STRING)
                                .description("회원의 성별")
                )
        ));
    }

    List<MemberAddressQueryDto> getMemberAddressList(int cnt) {
        List<MemberAddressQueryDto> memberAddressQueryList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            memberAddressQueryList.add(MemberAddressQueryDto.fromEntity(MemberAddress.builder()
                    .id((long) i + 1)
                    .address(address)
                    .isDefault(isDefault)
                    .member(member)
                    .build()));
        }
        return memberAddressQueryList;
    }
}