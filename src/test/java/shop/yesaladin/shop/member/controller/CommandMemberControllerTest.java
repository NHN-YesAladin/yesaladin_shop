package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
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
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.exception.AlreadyBlockedMemberException;
import shop.yesaladin.shop.member.exception.AlreadyUnblockedMemberException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

@AutoConfigureRestDocs
@WebMvcTest(CommandMemberController.class)
class CommandMemberControllerTest {

    private final String NAME = "Ramos";
    private final String NICKNAME = "Ramos";
    private final String LOGIN_ID = "testloginid";
    private final String PHONE = "01012345678";
    private final String INVALID_PASSWORD = "asdfasdf";
    private final String PASSWORD = "testPassword12@";
    private final String BIRTH = "20230107";
    private final String EMAIL = "test@test.com";
    private final String GENDER = "MALE";

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandMemberService commandMemberService;
    private Member member;
    private MemberCreateResponseDto createResponse;
    private MemberUpdateResponseDto updateResponse;
    private MemberWithdrawResponseDto withdrawResponse;

    private static Stream<Arguments> updateMemberRequestData() {
        return Stream.of(
                Arguments.of("'   '", "빈칸인 경우"),
                Arguments.of("'ㅇ'", "2자리 미만인 경우"),
                Arguments.of("'mongmeo21'", "숫자가 포함된 경우"),
                Arguments.of("'몽매오Ω≈ΩZ'", "특수문자가 포함된 경우"),
                Arguments.of("'hanadoolsetnetdasut'", "15자리 초과한 경우")
        );
    }

    private final String ROLE_MEMBER = "ROLE_MEMBER";

    @BeforeEach
    void setUp() {
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
        createResponse = MemberCreateResponseDto.fromEntity(member, role);
        updateResponse = MemberUpdateResponseDto.fromEntity(member);
    }

    @WithMockUser
    @Test
    @DisplayName("회원 등록 요청 시 입력 데이터가 null거나 @Valid 검증 조건에 맞지 않은 경우 요청에 실패 한다.")
    void signUpMember_withInvalidInputData() throws Exception {
        //given
        MemberCreateRequestDto request = new MemberCreateRequestDto();
        Mockito.when(commandMemberService.create(any())).thenReturn(createResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).create(any());
    }

    @WithMockUser
    @Test
    @DisplayName("회원 등록 요청 시 nickname, loginId, password에 걸려있는 정규 표현식에 부합하지 않는 경우 요청에 실패 한다.")
    void signUpMember_withInvalidInputData_invalidRegex() throws Exception {
        //given
        MemberCreateRequestDto request = new MemberCreateRequestDto(
                NAME,
                NICKNAME,
                LOGIN_ID,
                INVALID_PASSWORD,
                PHONE,
                BIRTH,
                EMAIL,
                GENDER
        );
        Mockito.when(commandMemberService.create(any())).thenReturn(createResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).create(any());
    }

    @WithMockUser
    @Test
    @DisplayName("회원 가입 성공")
    void signUpMember() throws Exception {
        //given
        MemberCreateRequestDto request = new MemberCreateRequestDto(
                NAME,
                NICKNAME,
                LOGIN_ID,
                EMAIL,
                PHONE,
                PASSWORD,
                BIRTH,
                GENDER
        );
        Member member = request.toEntity();

        Mockito.when(commandMemberService.create(any())).thenReturn(createResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.data.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.role", equalTo(ROLE_MEMBER)))
                .andExpect(jsonPath("$.data.memberGrade", equalTo(MemberGrade.WHITE.getName())));

        verify(commandMemberService, times(1)).create(any());

        //docs
        perform.andDo(document(
                "register-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("phone").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("회원의 패스워드"),
                        fieldWithPath("birth").type(JsonFieldType.STRING)
                                .description("회원의 생년월일"),
                        fieldWithPath("gender").type(JsonFieldType.STRING)
                                .description("회원의 성별")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원의 pk"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("data.loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("data.memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("data.role").type(JsonFieldType.STRING)
                                .description("회원의 권한"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메시지")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 정보 수정 실패 - body 가 null 인 경우")
    void updateMember_withNull() throws Exception {
        //given
        String loginId = "user@1";

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}", loginId)
                .with(csrf()));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).update(any(), any());
    }

    @WithMockUser
    @ParameterizedTest(name = "{1} : {0}")
    @MethodSource(value = "updateMemberRequestData")
    @DisplayName("회원정보수정 실패 - @Valid 검증 조건에 맞지 않은 경우")
    void updateMember_withInvalidInputData_forParametrizedTest(String nickname, String info)
            throws Exception {
        //given
        String loginId = "user@1";

        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                nickname
        );
        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        verify(commandMemberService, never()).update(anyString(), any());
    }

    @WithMockUser
    @Test
    @DisplayName("회원 정보 수정 실패-유효하지 않은 요청")
    void updateMember_withInvalidInputData() throws Exception {
        //given
        String loginId = "user@1";
        String nickname = "";

        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                nickname
        );
        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        verify(commandMemberService, never()).update(anyString(), any());

        //docs
        perform.andDo(document(
                "update-member-fail-validation-failed",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경할 닉네임")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원정보수정 실패 - 존재하지 않는 회원인 경우")
    void updateMember_withInvalidMemberId() throws Exception {
        //given
        String loginId = "user@1";

        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                NICKNAME
        );
        ArgumentCaptor<MemberUpdateRequestDto> requestArgumentCaptor = ArgumentCaptor.forClass(
                MemberUpdateRequestDto.class);

        Mockito.when(commandMemberService.update(eq(loginId), any()))
                .thenThrow(new MemberNotFoundException("Member loginId: " + loginId));

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        verify(commandMemberService, times(1)).update(anyString(), requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getNickname()).isEqualTo(request.getNickname());

        //docs
        perform.andDo(document(
                "update-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경할 닉네임")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMember() throws Exception {
        //given
        String loginId = "user@1";

        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                NICKNAME
        );
        ArgumentCaptor<MemberUpdateRequestDto> requestArgumentCaptor = ArgumentCaptor.forClass(
                MemberUpdateRequestDto.class);

        Mockito.when(commandMemberService.update(eq(loginId), any())).thenReturn(updateResponse);

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isOk());

        verify(commandMemberService, times(1)).update(anyString(), requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getNickname()).isEqualTo(request.getNickname());

        //docs
        perform.andDo(document(
                "update-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("회원의 수정할 닉네임")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원의 닉네임"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단 실패 - 유효하지 않은 요청 번수")
    void blockMember_fail_validationError() throws Exception {
        //given
        String loginId = "user@1";

        String blockedReason = "";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}/block", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Validation")));

        //docs
        perform.andDo(document(
                "block-member-fail-validation-error",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                        .description("차단 사유")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단 실패 - 존재하지 않는 회원인 경우")
    void blockMember_withInvalidMemberId() throws Exception {
        //given
        String loginId = "user@1";

        String blockedReason = "You are bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        Mockito.when(commandMemberService.block(eq(loginId), any()))
                .thenThrow(new MemberNotFoundException("Member loginId: " + loginId));

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}/block", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Member not found")));

        ArgumentCaptor<MemberBlockRequestDto> captor = ArgumentCaptor.forClass(MemberBlockRequestDto.class);
        verify(commandMemberService, times(1)).block(anyString(), captor.capture());
        assertThat(captor.getValue().getBlockedReason()).isEqualTo(blockedReason);

        //docs
        perform.andDo(document(
                "block-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                        .description("차단 사유")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단 실패 - 이미 차단된 회원인 경우")
    void blockMember_fail_alreadyBlockedMember() throws Exception {
        //given
        String loginId = "user@1";

        String blockedReason = "You are bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        Mockito.when(commandMemberService.block(eq(loginId), any()))
                .thenThrow(new AlreadyBlockedMemberException(loginId));

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}/block", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Already Blocked Member")));

        ArgumentCaptor<MemberBlockRequestDto> captor = ArgumentCaptor.forClass(MemberBlockRequestDto.class);
        verify(commandMemberService, times(1)).block(anyString(), captor.capture());
        assertThat(captor.getValue().getBlockedReason()).isEqualTo(blockedReason);

        //docs
        perform.andDo(document(
                "block-member-fail-already-blocked",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                        .description("차단 사유")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단 성공")
    void blockMember() throws Exception {
        //given
        long memberId = 1L;
        String name = "사용자";
        String loginId = "user@1";
        LocalDate blockedDate = LocalDate.now();
        String blockedReason = "You are bad guy";

        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        MemberBlockResponseDto response = ReflectionUtils.newInstance(
                MemberBlockResponseDto.class,
                memberId,
                name,
                loginId,
                true,
                blockedDate,
                blockedReason
        );
        Mockito.when(commandMemberService.block(eq(loginId), any())).thenReturn(response);

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}/block", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) memberId)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.loginId", equalTo(loginId)))
                .andExpect(jsonPath("$.isBlocked", equalTo(true)))
                .andExpect(jsonPath("$.blockedDate", equalTo(blockedDate.toString())))
                .andExpect(jsonPath("$.blockedReason", equalTo(blockedReason)));

        ArgumentCaptor<MemberBlockRequestDto> captor = ArgumentCaptor.forClass(MemberBlockRequestDto.class);
        verify(commandMemberService, times(1)).block(anyString(), captor.capture());
        assertThat(captor.getValue().getBlockedReason()).isEqualTo(blockedReason);

        //docs
        perform.andDo(document(
                "block-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestFields(fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                        .description("차단 사유")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("isBlocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단 여부"),
                        fieldWithPath("blockedDate").type(JsonFieldType.STRING).description("차단일"),
                        fieldWithPath("blockedReason").type(JsonFieldType.STRING)
                                .description("차단 사유")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단해지 실패 - 존재하지 않는 회원인 경우")
    void unblockMember_withInvalidMemberId() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(commandMemberService.unblock(loginId))
                .thenThrow(new MemberNotFoundException("Member loginId: " + loginId));

        //when
        ResultActions perform = mockMvc.perform(put(
                "/v1/members/{loginId}/unblock",
                loginId
        ).with(csrf()).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Member not found")));

        verify(commandMemberService, times(1)).unblock(loginId);

        //docs
        perform.andDo(document(
                "unblock-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단 해지 실패 - 이미 차단 해지된 회원인 경우")
    void unblockMember_fail_alreadyBlockedMember() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(commandMemberService.unblock(loginId))
                .thenThrow(new AlreadyUnblockedMemberException(loginId));

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{loginId}/unblock", loginId)
                .with(csrf()));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Already Unblocked Member")));

        verify(commandMemberService, times(1)).unblock(loginId);

        //docs
        perform.andDo(document(
                "unblock-member-fail-already-unblocked",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("회원 차단해지 성공")
    void unblockMember() throws Exception {
        //given
        long memberId = 1L;
        String name = "사용자";
        String loginId = "user@1";
        LocalDate unblockedDate = LocalDate.now();

        MemberUnblockResponseDto response = ReflectionUtils.newInstance(
                MemberUnblockResponseDto.class,
                memberId,
                name,
                loginId,
                false,
                unblockedDate
        );
        Mockito.when(commandMemberService.unblock(loginId)).thenReturn(response);

        //when
        ResultActions perform = mockMvc.perform(put(
                "/v1/members/{loginId}/unblock",
                loginId
        ).with(csrf()).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) memberId)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.loginId", equalTo(loginId)))
                .andExpect(jsonPath("$.isBlocked", equalTo(false)))
                .andExpect(jsonPath("$.unblockedDate", equalTo(unblockedDate.toString())));

        verify(commandMemberService, times(1)).unblock(loginId);

        //docs
        perform.andDo(document(
                "unblock-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("isBlocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단 여부"),
                        fieldWithPath("unblockedDate").type(JsonFieldType.STRING)
                                .description("차단 해지일")
                )
        ));
    }

    @WithMockUser
    @Test
    void withdrawMember_fail_invalidMember() throws Exception {
        //given
        String invalidLoginId = "invalidLoginId";

        Mockito.when(commandMemberService.withDraw(invalidLoginId))
                .thenThrow(new MemberNotFoundException("Member loginId: " + invalidLoginId));

        //when
        ResultActions perform = mockMvc.perform(delete(
                "/v1/members/withdraw/{loginId}",
                invalidLoginId
        )
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verify(commandMemberService, times(1)).withDraw(invalidLoginId);

        //docs
        perform.andDo(document(
                "withdraw-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("탈퇴할 회원의 아이디")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    void withdrawMember() throws Exception {
        String loginId = "loginId";

        Member withdrawMember = Member.builder()
                .id(1L)
                .name("deleted-1")
                .isWithdrawal(true)
                .withdrawalDate(LocalDate.now())
                .build();

        withdrawResponse = MemberWithdrawResponseDto.fromEntity(withdrawMember);

        Mockito.when(commandMemberService.withDraw(loginId))
                .thenReturn(withdrawResponse);

        //when
        ResultActions perform = mockMvc.perform(delete("/v1/members/withdraw/{loginId}", loginId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", equalTo(withdrawMember.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(withdrawMember.getName())))
                .andExpect(jsonPath("$.data.withdrawal", equalTo(withdrawMember.isWithdrawal())))
                .andExpect(jsonPath(
                        "$.data.withdrawalDate",
                        equalTo(withdrawMember.getWithdrawalDate().toString())
                ));

        verify(commandMemberService, times(1)).withDraw(loginId);

        //docs
        perform.andDo(document(
                "withdraw-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("탈퇴할 회원의 아이디")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("data.withdrawal").type(JsonFieldType.BOOLEAN)
                                .description("회원의 탈퇴 여부"),
                        fieldWithPath("data.withdrawalDate").type(JsonFieldType.STRING)
                                .description("회원의 탈퇴일"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메시지")
                                .optional()
                )
        ));
    }
}