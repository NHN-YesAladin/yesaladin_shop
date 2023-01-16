package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

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
        mockMvc.perform(get("/v1/members/login/{loginId}", loginId))
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
    }
}