package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

@Disabled
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("local-test")
public class CommandMemberControllerSecurityTest {

    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    private final String NAME = "Ramos";
    private final String NICKNAME = "Ramos";
    private final String LOGIN_ID = "testloginid";
    private Member member;
    @MockBean
    private CommandMemberService commandMemberService;

    @BeforeEach
    void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        long id = 1L;
        int roleId = 1;

        member = Member.builder()
                .id(id)
                .name(NAME)
                .nickname(NICKNAME)
                .loginId(LOGIN_ID)
                .memberGrade(MemberGrade.WHITE)
                .build();
        Role role = Role.builder().id(roleId).name("ROLE_MEMBER").build();
    }


    @WithAnonymousUser
    @Test
    @DisplayName("회원 차단 해지 실패 - 차단 해지 권한 없는 경우")
    void unblockMember_fail_unauthorized() throws Exception {
        //given
        String loginId = member.getLoginId();

        //when
        ResultActions result = mockMvc.perform(put("/v1/members/{loginId}/unblock", loginId)
                .with(csrf()));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.FORBIDDEN.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.FORBIDDEN.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "unblock-member-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("회원의 아이디")
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

    @WithMockUser
    @Test
    @DisplayName("회원 차단 실패 - 차단 권한 없는 경우")
    void blockMember_fail_unauthorized() throws Exception {
        //given
        String loginId = member.getLoginId();
        String blockedReason = "you are a bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        //when
        ResultActions result = mockMvc.perform(put("/v1/members/{loginId}/block", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.FORBIDDEN.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.FORBIDDEN.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "block-member-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("회원의 아이디")
                ),
                requestFields(fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                        .description("차단 사유")),
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
}
