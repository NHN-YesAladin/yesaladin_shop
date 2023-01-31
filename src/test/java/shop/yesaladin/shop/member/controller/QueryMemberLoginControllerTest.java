package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

@AutoConfigureRestDocs
@WebMvcTest(QueryMemberLoginController.class)
class QueryMemberLoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    QueryMemberService queryMemberService;

    @Test
    void doLogin_failed_whenMemberNotFound() throws Exception {
        //given
        String loginId = "testId";

        //when
        Mockito.when(queryMemberService.findMemberLoginInfoByLoginId(loginId))
                .thenThrow(MemberNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/members/login/{loginId}", loginId))
                .andExpect(status().is4xxClientError());

        verify(queryMemberService, times(1)).findMemberLoginInfoByLoginId(loginId);
    }

    @Test
    void doLogin() throws Exception {
        //given
        Long memberId = 1L;
        String memberName = "testName";
        String memberNickname = "testNickname";
        String loginId = "testId";
        String email = "test@test.com";
        String password = "testPassword";
        List<String> roles = List.of("ROLE_MEMBER", "ROLE_ADMIN");

        MemberLoginResponseDto response = new MemberLoginResponseDto(
                memberId,
                memberName,
                memberNickname,
                loginId,
                email,
                password,
                roles
        );

        //when
        Mockito.when(queryMemberService.findMemberLoginInfoByLoginId(loginId))
                .thenReturn(response);

        //then
        ResultActions resultActions = mockMvc.perform(get("/v1/members/login/{loginId}", loginId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", equalTo(memberId.intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(memberName)))
                .andExpect(jsonPath("$.data.nickname", equalTo(memberNickname)))
                .andExpect(jsonPath("$.data.loginId", equalTo(loginId)))
                .andExpect(jsonPath("$.data.email", equalTo(email)))
                .andExpect(jsonPath("$.data.password", equalTo(password)))
                .andExpect(jsonPath("$.data.roles", equalTo(roles)));

        verify(queryMemberService, times(1)).findMemberLoginInfoByLoginId(loginId);

        //docs
        resultActions.andDo(document(
                "doLogin",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("회원의 Login ID")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("회원의 PK"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("data.loginId").type(JsonFieldType.STRING)
                                .description("회원의 loginId"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                                .description("회원의 email"),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                                .description("회원의 password"),
                        fieldWithPath("data.roles").type(JsonFieldType.ARRAY)
                                .description("회원의 권한 리스트"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메시지")
                                .optional()
                )
        ));
    }
}