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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerListResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

@AutoConfigureRestDocs
@WebMvcTest(QueryMemberController.class)
class QueryMemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    QueryMemberService queryMemberService;

    @WithMockUser
    @Test
    void existsLoginId_whenNotExist_return_false() throws Exception {
        //given
        String loginId = "songs4805";

        //when
        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(false);

        //then
        mockMvc.perform(get("/v1/members/checkId/{loginId}", loginId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(false)))
                .andDo(print());

        verify(queryMemberService, times(1)).existsLoginId(loginId);
    }

    @WithMockUser
    @Test
    void existsLoginId() throws Exception {
        //given
        String loginId = "songs4805";

        //when
        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(true);

        //then
        ResultActions resultActions = mockMvc.perform(get("/v1/members/checkId/{loginId}", loginId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsLoginId(loginId);

        //docs
        resultActions.andDo(document(
                "existsLoginId",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("중복 체크 대상 loginId")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                                .description("loginId 중복 여부"),
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
    void existsNickname_whenNotExist_return_false() throws Exception {
        //given
        String nickname = "songs4805";

        //when
        Mockito.when(queryMemberService.existsNickname(nickname)).thenReturn(false);

        //then
        mockMvc.perform(get("/v1/members/checkNick/{nickname}", nickname))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(false)))
                .andDo(print());

        verify(queryMemberService, times(1)).existsNickname(nickname);
    }

    @WithMockUser
    @Test
    void existsNickname() throws Exception {
        //given
        String nickname = "songs4805";

        //when
        Mockito.when(queryMemberService.existsNickname(nickname)).thenReturn(true);

        //then
        ResultActions resultActions = mockMvc.perform(get(
                        "/v1/members/checkNick/{nickname}",
                        nickname
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsNickname(nickname);

        //docs
        resultActions.andDo(document(
                "existsNickname",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("nickname").description("중복 체크 대상 nickname")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                                .description("nickname 중복 여부"),
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
    void existsEmail_whenNotExist_return_false() throws Exception {
        //given
        String email = "test@test.com";

        //when
        Mockito.when(queryMemberService.existsEmail(email)).thenReturn(false);

        //then
        mockMvc.perform(get("/v1/members/checkEmail/{email}", email)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(false)))
                .andDo(print());

        verify(queryMemberService, times(1)).existsEmail(email);
    }

    @WithMockUser
    @Test
    void existsEmail() throws Exception {
        //given
        String email = "test@test.com";

        //when
        Mockito.when(queryMemberService.existsEmail(email)).thenReturn(true);

        //then
        ResultActions resultActions = mockMvc.perform(get("/v1/members/checkEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsEmail(email);

        //docs
        resultActions.andDo(document(
                "existsEmail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("email").description("중복 체크 대상 email")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                                .description("email 중복 여부"),
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
    void existsPhone_whenNotExist_return_false() throws Exception {
        //given
        String phone = "01011112222";

        //when
        Mockito.when(queryMemberService.existsPhone(phone)).thenReturn(false);

        //then
        mockMvc.perform(get("/v1/members/checkPhone/{phone}", phone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(false)))
                .andDo(print());

        verify(queryMemberService, times(1)).existsPhone(phone);
    }

    @WithMockUser
    @Test
    void existsPhone() throws Exception {
        //given
        String phone = "01011112222";

        //when
        Mockito.when(queryMemberService.existsPhone(phone)).thenReturn(true);

        //then
        ResultActions resultActions = mockMvc.perform(get("/v1/members/checkPhone/{phone}", phone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsPhone(phone);

        //docs
        resultActions.andDo(document(
                "existsPhone",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("phone").description("중복 체크 대상 phone")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                                .description("phone 중복 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 상태 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메시지")
                                .optional()
                )
        ));
    }

    @WithMockUser(username = "user@1")
    @Test
    void getMemberGrade_fail_invalidParameter() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getMemberGradeByLoginId(loginId))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members").param("type", "abcs"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_BAD_REQUEST.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-member-grade-fail-invalid-parameter",
                getDocumentRequest(),
                getDocumentResponse(),
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

    @WithMockUser(username = "user@1")
    @Test
    void getMemberGrade_fail_memberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getMemberGradeByLoginId(loginId))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members").param("type", "grade"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-member-grade-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
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

    @WithMockUser(username = "user@1")
    @Test
    void getMemberGrade_success() throws Exception {
        //given
        String loginId = "user@1";
        MemberGrade memberGrade = MemberGrade.WHITE;

        MemberGradeQueryResponseDto response = ReflectionUtils.newInstance(
                MemberGradeQueryResponseDto.class, memberGrade.name(), memberGrade.getName());

        Mockito.when(queryMemberService.getMemberGradeByLoginId(loginId)).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/members").param("type", "grade"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.gradeEn", equalTo(memberGrade.name())))
                .andExpect(jsonPath("$.data.gradeKo", equalTo(memberGrade.getName())));

        //docs
        result.andDo(document(
                "get-member-grade-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.gradeEn").type(JsonFieldType.STRING)
                                .description("회원 등급 영어 이름"),
                        fieldWithPath("data.gradeKo").type(JsonFieldType.STRING)
                                .description("회원 등급 한국어 이름"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser(username = "user@1")
    @Test
    void getMemberInfo_fail_memberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getByLoginId(loginId))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-member-info-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
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

    @WithMockUser(username = "user@1")
    @Test
    void getMemberInfo_success() throws Exception {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        MemberQueryResponseDto response = MemberQueryResponseDto.fromEntity(member);

        Mockito.when(queryMemberService.getByLoginId(loginId)).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/members"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.data.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.password", equalTo(member.getPassword())))
                .andExpect(jsonPath("$.data.birthYear", equalTo(member.getBirthYear())))
                .andExpect(jsonPath("$.data.birthMonth", equalTo(member.getBirthMonth())))
                .andExpect(jsonPath("$.data.birthDay", equalTo(member.getBirthDay())))
                .andExpect(jsonPath("$.data.email", equalTo(member.getEmail())))
                .andExpect(jsonPath(
                        "$.data.signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.grade", equalTo(member.getMemberGrade().getName())))
                .andExpect(jsonPath(
                        "$.data.gender",
                        equalTo(member.getMemberGenderCode().getGender() == 1 ? "남" : "여")
                ));

        //docs
        result.andDo(document(
                "get-member-info-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("회원의 PK"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("data.loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("data.birthYear").type(JsonFieldType.NUMBER)
                                .description("회원의 생년"),
                        fieldWithPath("data.birthMonth").type(JsonFieldType.NUMBER)
                                .description("회원의 생월"),
                        fieldWithPath("data.birthDay").type(JsonFieldType.NUMBER)
                                .description("회원의 생일"),
                        fieldWithPath("data.signUpDate").type(JsonFieldType.STRING)
                                .description("회원의 가입일"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("data.grade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                .description("회원의 성별"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    void manageMemberInfoByLoginId_fail_MemberNotFound() throws Exception {
        //given
        String loginId = "loginId";
        Mockito.when(queryMemberService.findMemberManageByLoginId(loginId))
                .thenThrow(new MemberNotFoundException("Member LoginId: " + loginId));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "loginid",
                loginId
        ));
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void manageMemberInfoByLoginId() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManageByLoginId(loginId)).thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "loginid",
                loginId
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.withdrawalDate", equalTo(null)))
                .andExpect(jsonPath("$.data.isWithdrawal", equalTo(member.isWithdrawal())))
                .andExpect(jsonPath("$.data.isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfoByNickname_fail_MemberNotFound() throws Exception {
        //given
        String nickname = "nickname";
        Mockito.when(queryMemberService.findMemberManageByNickName(nickname))
                .thenThrow(new MemberNotFoundException("Member Nickname: " + nickname));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "nickname",
                nickname
        ));
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void manageMemberInfoByNickname() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManageByNickName(member.getNickname()))
                .thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "nickname",
                member.getNickname()
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.withdrawalDate", equalTo(null)))
                .andExpect(jsonPath("$.data.isWithdrawal", equalTo(member.isWithdrawal())))
                .andExpect(jsonPath("$.data.isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfoByPhone_fail_MemberNotFound() throws Exception {
        //given
        String phone = "phone";
        Mockito.when(queryMemberService.findMemberManageByPhone(phone))
                .thenThrow(new MemberNotFoundException("Member Phone: " + phone));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "phone", phone
        ));
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void manageMemberInfoByPhone() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManageByPhone(member.getPhone()))
                .thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "phone",
                member.getPhone()
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.withdrawalDate", equalTo(null)))
                .andExpect(jsonPath("$.data.isWithdrawal", equalTo(member.isWithdrawal())))
                .andExpect(jsonPath("$.data.isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfosByName_fail_MemberNotFound() throws Exception {
        //given
        String phone = "phone";
        Mockito.when(queryMemberService.findMemberManageByPhone(phone))
                .thenThrow(new MemberNotFoundException("Member Name: " + phone));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/member/manage").param("name", phone));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    void manageMemberInfosByName() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesByName(member.getName(), 0, 10))
                .thenReturn(MemberManagerListResponseDto.builder()
                        .count(1L)
                        .memberManagerResponseDtoList(List.of(responseDto))
                        .build());

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "name",
                member.getName()
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.memberManagerResponseDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].loginId",
                        equalTo(member.getLoginId())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].nickname",
                        equalTo(member.getNickname())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].email",
                        equalTo(member.getEmail())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].phone",
                        equalTo(member.getPhone())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].name",
                        equalTo(member.getName())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].withdrawalDate",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].isBlocked",
                        equalTo(member.isBlocked())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].blockedReason",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].blockedDate",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].unblockedDate",
                        equalTo(null)
                ));
    }

    @WithMockUser
    @Test
    void manageMemberInfosBySignUp_MemberNotFound() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        Mockito.when(queryMemberService.findMemberManagesBySignUpDate(member.getSignUpDate(), 0, 10))
                .thenThrow(new MemberNotFoundException("Member SignUpDate: " + member.getSignUpDate()));

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/member/manage").param("signupdate",
                member.getSignUpDate().format(DateTimeFormatter.ISO_DATE)));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    void manageMemberInfosBySignUpDate() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesBySignUpDate(member.getSignUpDate(), 0, 10))
                .thenReturn(MemberManagerListResponseDto.builder()
                        .count(1L)
                        .memberManagerResponseDtoList(List.of(responseDto))
                        .build());

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "signupdate",
                member.getSignUpDate().format(DateTimeFormatter.ISO_DATE)
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.count", equalTo(1)))
                .andExpect(jsonPath("$.data.memberManagerResponseDtoList[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].loginId",
                        equalTo(member.getLoginId())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].nickname",
                        equalTo(member.getNickname())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].email",
                        equalTo(member.getEmail())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].phone",
                        equalTo(member.getPhone())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].name",
                        equalTo(member.getName())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].withdrawalDate",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].isBlocked",
                        equalTo(member.isBlocked())
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].blockedReason",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].blockedDate",
                        equalTo(null)
                ))
                .andExpect(jsonPath(
                        "$.data.memberManagerResponseDtoList[0].unblockedDate",
                        equalTo(null)
                ));
    }
}