package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawRequestDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
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
    private MemberBlockResponseDto blockResponse;
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
        blockResponse = MemberBlockResponseDto.fromEntity(member);
    }

    @Test
    @DisplayName("회원 등록 요청 시 입력 데이터가 null거나 @Valid 검증 조건에 맞지 않은 경우 요청에 실패 한다.")
    void signUpMember_withInvalidInputData() throws Exception {
        //given
        MemberCreateRequestDto request = new MemberCreateRequestDto();
        Mockito.when(commandMemberService.create(any())).thenReturn(createResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).create(any());
    }

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
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).create(any());
    }

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
        ResultActions perform = mockMvc.perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.role", equalTo(ROLE_MEMBER)));

        verify(commandMemberService, times(1)).create(any());
    }

    @Test
    @DisplayName("회원 정보 수정 실패 - body 가 null 인 경우")
    void updateMember_withNull() throws Exception {
        //given
        Long memberId = 1L;

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}", memberId));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandMemberService, never()).update(any(), any());
    }

    @ParameterizedTest(name = "{1} : {0}")
    @MethodSource(value = "updateMemberRequestData")
    @DisplayName("회원정보수정 실패 - @Valid 검증 조건에 맞지 않은 경우")
    void updateMember_withInvalidInputData_forParametrizedTest(String nickname, String info)
            throws Exception {
        //given
        Long memberId = 1L;
        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                nickname
        );
        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}", memberId).contentType(
                        MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        verify(commandMemberService, never()).update(anyLong(), any());
    }

    @Test
    void updateMember_withInvalidInputData() throws Exception {
        //given
        Long memberId = 1L;
        String nickname = "";

        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                nickname
        );
        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}", memberId).contentType(
                        MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        verify(commandMemberService, never()).update(anyLong(), any());

        //docs
        perform.andDo(document(
                "update-member-fail-validation-failed",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원정보수정 실패 - 존재하지 않는 회원인 경우")
    void updateMember_withInvalidMemberId() throws Exception {
        //given
        long memberId = 1L;
        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                NICKNAME
        );
        ArgumentCaptor<MemberUpdateRequestDto> requestArgumentCaptor = ArgumentCaptor.forClass(
                MemberUpdateRequestDto.class);

        Mockito.when(commandMemberService.update(eq(memberId), any()))
                .thenThrow(new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions perform = mockMvc.perform(put(
                "/v1/members/{memberId}",
                memberId
        ).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        verify(commandMemberService, times(1)).update(anyLong(), requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getNickname()).isEqualTo(request.getNickname());

        //docs
        perform.andDo(document(
                "update-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMember() throws Exception {
        //given
        long memberId = 1L;
        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                NICKNAME
        );
        ArgumentCaptor<MemberUpdateRequestDto> requestArgumentCaptor = ArgumentCaptor.forClass(
                MemberUpdateRequestDto.class);

        Mockito.when(commandMemberService.update(eq(memberId), any())).thenReturn(updateResponse);

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}", memberId).contentType(
                        MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isOk());

        verify(commandMemberService, times(1)).update(anyLong(), requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getNickname()).isEqualTo(request.getNickname());

        //docs
        perform.andDo(document(
                "update-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
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

    @Test
    @DisplayName("회원 차단 실패 - 존재하지 않는 회원인 경우")
    void blockMember_withInvalidMemberId() throws Exception {
        //given
        long memberId = 1L;
        Mockito.when(commandMemberService.block(memberId))
                .thenThrow(new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}/block", memberId));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Member not found")));
        ;

        verify(commandMemberService, times(1)).block(memberId);

        //docs
        perform.andDo(document(
                "block-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원 차단 성공")
    void blockMember() throws Exception {
        //given
        long memberId = 1L;
        Mockito.when(commandMemberService.block(memberId)).thenReturn(blockResponse);

        //when
        ResultActions perform = mockMvc.perform(put("/v1/members/{memberId}/block", memberId));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) memberId)))
                .andExpect(jsonPath("$.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.blocked", equalTo(member.isBlocked())));

        verify(commandMemberService, times(1)).block(memberId);

        //docs
        perform.andDo(document(
                "block-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단 여부")
                )
        ));
    }

    @Test
    @DisplayName("회원 차단해지 실패 - 존재하지 않는 회원인 경우")
    void unblockMember_withInvalidMemberId() throws Exception {
        //given
        long memberId = 1L;
        Mockito.when(commandMemberService.unblock(memberId))
                .thenThrow(new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions perform = mockMvc.perform(put(
                "/v1/members/{memberId}/unblock",
                memberId
        ).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Matchers.startsWith("Member not found")));

        verify(commandMemberService, times(1)).unblock(memberId);

        //docs
        perform.andDo(document(
                "unblock-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원 차단해지 성공")
    void unblockMember() throws Exception {
        //given
        long memberId = 1L;
        Mockito.when(commandMemberService.unblock(memberId)).thenReturn(blockResponse);

        //when
        ResultActions perform = mockMvc.perform(put(
                "/v1/members/{memberId}/unblock",
                memberId
        ).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) memberId)))
                .andExpect(jsonPath("$.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.blocked", equalTo(member.isBlocked())));

        verify(commandMemberService, times(1)).unblock(memberId);

        //docs
        perform.andDo(document(
                "unblock-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단 여부")
                )
        ));
    }

    @Test
    void withdrawMember_fail_invalidMember() throws Exception {
        //given
        String invalidLoginId = "invalidLoginId";

        MemberWithdrawRequestDto request = ReflectionUtils.newInstance(
                MemberWithdrawRequestDto.class,
                invalidLoginId
        );

        Mockito.when(commandMemberService.withDraw(request.getLoginId()))
                .thenThrow(new MemberNotFoundException("Member loginId: " + request.getLoginId()));

        //when
        ResultActions perform = mockMvc.perform(delete("/v1/members/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        verify(commandMemberService, times(1)).withDraw(invalidLoginId);

        //docs
        perform.andDo(document(
                "withdraw-member-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                .description("탈퇴할 회원의 아이디")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void withdrawMember() throws Exception {
        String loginId = "loginId";

        MemberWithdrawRequestDto request = ReflectionUtils.newInstance(
                MemberWithdrawRequestDto.class,
                loginId
        );

        Member withdrawMember = Member.builder()
                .id(1L)
                .name("deleted-1")
                .isWithdrawal(true)
                .withdrawalDate(LocalDate.now())
                .build();

        withdrawResponse = MemberWithdrawResponseDto.fromEntity(withdrawMember);

        Mockito.when(commandMemberService.withDraw(request.getLoginId()))
                .thenReturn(withdrawResponse);

        //when
        ResultActions perform = mockMvc.perform(delete("/v1/members/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(withdrawMember.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(withdrawMember.getName())))
                .andExpect(jsonPath("$.withdrawal", equalTo(withdrawMember.isWithdrawal())))
                .andExpect(jsonPath(
                        "$.withdrawalDate",
                        equalTo(withdrawMember.getWithdrawalDate().toString())
                ));

        verify(commandMemberService, times(1)).withDraw(loginId);

        //docs
        perform.andDo(document(
                "withdraw-member-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                .description("탈퇴할 회원의 아이디")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                        fieldWithPath("withdrawal").type(JsonFieldType.BOOLEAN).description("회원의 탈퇴 여부"),
                        fieldWithPath("withdrawalDate").type(JsonFieldType.STRING)
                                .description("회원의 탈퇴일")
                )
        ));
    }
}