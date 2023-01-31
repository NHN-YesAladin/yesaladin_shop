package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
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

    @WithMockUser
    @Test
    void getMemberAddressByMemberId_fail_MemberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberAddressService.findByLoginId(loginId))
                .thenThrow(new MemberNotFoundException("Member loginId: " + loginId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/addresses"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        //docs
        result.andDo(document(
                "get-member-address-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    void getMemberAddressByMemberId() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberAddressService.findByLoginId(loginId))
                .thenReturn(getMemberAddressList(10, loginId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/addresses"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].address", equalTo(address)))
                .andExpect(jsonPath("$[0].isDefault", equalTo(isDefault)))
                .andExpect(jsonPath("$[0].loginId", equalTo(loginId)));

        //docs
        result.andDo(document(
                "get-member-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("등록된 배송지 Pk"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING)
                                .description("등록된 배송지 주소"),
                        fieldWithPath("[].isDefault").type(JsonFieldType.BOOLEAN)
                                .description("등록된 배송지의 대표주소 여부"),
                        fieldWithPath("[].loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디")
                )
        ));
    }

    List<MemberAddressResponseDto> getMemberAddressList(int cnt, String loginId) {
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        List<MemberAddressResponseDto> memberAddressQueryList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            memberAddressQueryList.add(MemberAddressResponseDto.fromEntity(MemberAddress.builder()
                    .id((long) i + 1)
                    .address(address)
                    .isDefault(isDefault)
                    .member(member)
                    .build()));
        }
        return memberAddressQueryList;
    }
}