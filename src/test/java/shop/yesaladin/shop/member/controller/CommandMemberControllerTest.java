package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

@WebMvcTest(CommandMemberController.class)
class CommandMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CommandMemberService commandMemberService;

    private Member member;
    private MemberCreateResponse response;

    private final String NAME = "Ramos";
    private final String NICKNAME = "Ramos";
    private final String LOGIN_ID = "testloginid";
    private final String INVALID_PASSWORD = "asdfasdf";
    private final String PASSWORD = "testPassword12@";
    private final String BIRTH = "20230107";
    private final String EMAIL = "test@test.com";
    private final String GENDER = "MALE";

    @BeforeEach
    void setUp() {
        long id = 1L;
        member = Member.builder().id(id).name(NAME).nickname(NICKNAME).loginId(LOGIN_ID).build();
        response = MemberCreateResponse.fromEntity(member);
    }

    @Test
    @DisplayName("회원 등록 요청 시 입력 데이터가 null거나 @Valid 검증 조건에 맞지 않은 경우 요청에 실패 한다.")
    void signUpMember_withInvalidInputData() throws Exception {
        //given
        MemberCreateRequest request = new MemberCreateRequest();
        given(commandMemberService.create(any())).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandMemberService, never()).create(any());
    }

    @Test
    @DisplayName("회원 등록 요청 시 nickname, loginId, password에 걸려있는 정규 표현식에 부합하지 않는 경우 요청에 실패 한다.")
    void signUpMember_withInvalidInputData_invalidRegex() throws Exception {
        //given
        MemberCreateRequest request = new MemberCreateRequest(
                NAME,
                NICKNAME,
                LOGIN_ID,
                INVALID_PASSWORD,
                BIRTH,
                EMAIL,
                GENDER
        );
        given(commandMemberService.create(any())).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandMemberService, never()).create(any());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void signUpMember() throws Exception {
        //given
        MemberCreateRequest request = new MemberCreateRequest(
                NAME,
                NICKNAME,
                LOGIN_ID,
                PASSWORD,
                BIRTH,
                EMAIL,
                GENDER
        );
        Member member = request.toEntity(null);

        given(commandMemberService.create(any())).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.loginId", equalTo(member.getLoginId())));

        verify(commandMemberService, times(1)).create(any());
    }
}