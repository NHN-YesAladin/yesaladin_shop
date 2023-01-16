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
                .andExpect(jsonPath("$.id", equalTo(memberId.intValue())))
                .andExpect(jsonPath("$.name", equalTo(memberName)))
                .andExpect(jsonPath("$.nickname", equalTo(memberNickname)))
                .andExpect(jsonPath("$.loginId", equalTo(loginId)))
                .andExpect(jsonPath("$.email", equalTo(email)))
                .andExpect(jsonPath("$.password", equalTo(password)))
                .andExpect(jsonPath("$.role", equalTo(roles)));

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
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("회원의 PK"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                .description("회원의 loginId"),
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("회원의 email"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("회원의 password"),
                        fieldWithPath("role").type(JsonFieldType.ARRAY)
                                .description("회원의 권한 리스트")
                )
        ));
    }
}