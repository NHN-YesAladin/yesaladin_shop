package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberStatisticsResponseDto;
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
        Mockito.when(queryMemberService.getMemberGradeByLoginId(any()))
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
        MemberGrade memberGrade = MemberGrade.WHITE;

        MemberGradeQueryResponseDto response = ReflectionUtils.newInstance(
                MemberGradeQueryResponseDto.class, memberGrade.name(), memberGrade.getName());

        Mockito.when(queryMemberService.getMemberGradeByLoginId(any())).thenReturn(response);

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

        Mockito.when(queryMemberService.getByLoginId(any()))
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

        Mockito.when(queryMemberService.getByLoginId(any())).thenReturn(response);

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
                .andExpect(jsonPath("$.data.phone", equalTo(member.getPhone())))
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
                        fieldWithPath("data.phone").type(JsonFieldType.STRING)
                                .description("회원의 전화번호"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    void manageMemberInfo() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManages(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage"));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfoByLoginId() throws Exception {
        //given
        String loginId = "loginId";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesByLoginId(loginId, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "loginid",
                loginId
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }


    @WithMockUser
    @Test
    void manageMemberInfoByNickname() throws Exception {
        //given
        String nickname = "nickname";
        Member member = MemberDummy.dummyWithLoginIdAndId(nickname);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesByNickName(
                        nickname,
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "nickname",
                member.getNickname()
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfoByPhone() throws Exception {

        //given
        String phone = "phone";
        Member member = MemberDummy.dummyWithLoginIdAndId(phone);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesByPhone(phone, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 1), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "phone",
                phone
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfosByName() throws Exception {
        //given
        String name = "name";
        Member member = MemberDummy.dummyWithLoginIdAndId(name);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesByName(
                name,
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 10), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "name",
                name
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    void manageMemberInfosBySignUpDate() throws Exception {
        //given
        String name = "name";
        Member member = MemberDummy.dummyWithLoginIdAndId(name);
        MemberManagerResponseDto responseDto = MemberManagerResponseDto.fromEntity(member);
        Mockito.when(queryMemberService.findMemberManagesBySignUpDate(
                member.getSignUpDate(),
                PageRequest.of(0, 10)
        )).thenReturn(new PageImpl<>(List.of(responseDto), PageRequest.of(0, 10), 1L));

        ResultActions resultActions = mockMvc.perform(get("/v1/members/manage").param(
                "signupdate",
                member.getSignUpDate().toString()
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.dataList[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data.dataList[0].loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.data.dataList[0].nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.data.dataList[0].email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.data.dataList[0].phone", equalTo(member.getPhone())))
                .andExpect(jsonPath("$.data.dataList[0].name", equalTo(member.getName())))
                .andExpect(jsonPath(
                        "$.data.dataList[0].signUpDate",
                        equalTo(member.getSignUpDate().toString())
                ))
                .andExpect(jsonPath("$.data.dataList[0].withdrawalDate", equalTo(null)))
                .andExpect(jsonPath(
                        "$.data.dataList[0].isWithdrawal",
                        equalTo(member.isWithdrawal())
                ))
                .andExpect(jsonPath("$.data.dataList[0].isBlocked", equalTo(member.isBlocked())))
                .andExpect(jsonPath("$.data.dataList[0].blockedReason", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].blockedDate", equalTo(null)))
                .andExpect(jsonPath("$.data.dataList[0].unblockedDate", equalTo(null)));
    }

    @WithMockUser
    @Test
    @DisplayName("n일 후 생일인 회원 조회 성공")
    void getBirthdayMemberTest() throws Exception {
        // given
        int laterDays = 3;
        long memberId = 1L;
        when(queryMemberService.findMemberIdsByBirthday(Mockito.anyInt())).thenReturn(List.of(new MemberIdDto(
                memberId)));

        // when
        ResultActions resultActions = mockMvc.perform(get("/v1/members")
                .queryParam("type", "birthday")
                .queryParam("laterDays", String.valueOf(laterDays))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.[0].memberId").value(memberId));

        // docs
        resultActions.andDo(document(
                "get-member-id-list-by-birthday-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("type")
                                .description("조회 조건을 생일(birthday)으로 지정 필수"),
                        parameterWithName("laterDays")
                                .description("오늘 날짜를 기준으로 생일을 계산할 일수")

                ),
                responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                .description("생일인 회원의 PK")
                )
        ));
    }

    @WithMockUser
    @Test
    void getMemberStatistics() throws Exception {
        //given
        long totalCount = 10L;
        long totalWithdraws = 1L;
        long emptyCount = 0L;
        MemberStatisticsResponseDto response = MemberStatisticsResponseDto.builder()
                .totalMembers(totalCount)
                .totalWithdrawMembers(totalWithdraws)
                .totalBlockedMembers(emptyCount)
                .totalWhiteGrades(totalCount)
                .totalBronzeGrades(emptyCount)
                .totalSilverGrades(emptyCount)
                .totalGoldGrades(emptyCount)
                .totalPlatinumGrades(emptyCount)
                .build();

        Mockito.when(queryMemberService.getMemberStatistics()).thenReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/members/statistics"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath(
                        "$.data.totalMembers",
                        equalTo(response.getTotalMembers().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalWithdrawMembers",
                        equalTo(response.getTotalWithdrawMembers().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalBlockedMembers",
                        equalTo(response.getTotalBlockedMembers().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalWhiteGrades",
                        equalTo(response.getTotalWhiteGrades().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalBronzeGrades",
                        equalTo(response.getTotalBronzeGrades().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalSilverGrades",
                        equalTo(response.getTotalSilverGrades().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalGoldGrades",
                        equalTo(response.getTotalGoldGrades().intValue())
                ))
                .andExpect(jsonPath(
                        "$.data.totalPlatinumGrades",
                        equalTo(response.getTotalPlatinumGrades().intValue())
                ));

        //docs
        resultActions.andDo(document(
                "get-member-statistics-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalMembers").type(JsonFieldType.NUMBER)
                                .description("전체 회원 수"),
                        fieldWithPath("data.totalWithdrawMembers").type(JsonFieldType.NUMBER)
                                .description("탈퇴 회원 수"),
                        fieldWithPath("data.totalBlockedMembers").type(JsonFieldType.NUMBER)
                                .description("차단된 회원 수"),
                        fieldWithPath("data.totalWhiteGrades").type(JsonFieldType.NUMBER)
                                .description("화이트 등급의 회원 수"),
                        fieldWithPath("data.totalBronzeGrades").type(JsonFieldType.NUMBER)
                                .description("브론즈 등급의 회원 수"),
                        fieldWithPath("data.totalSilverGrades").type(JsonFieldType.NUMBER)
                                .description("실버 등급의 회원 수"),
                        fieldWithPath("data.totalGoldGrades").type(JsonFieldType.NUMBER)
                                .description("골드 등급의 회원 수"),
                        fieldWithPath("data.totalPlatinumGrades").type(JsonFieldType.NUMBER)
                                .description("플래티넘 등급의 회원 수"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser(username = "user@1")
    @Test
    void getPassword_fail_memberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getByLoginId(any()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/password-check"));

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
                "get-member-password-fail-member-not-found",
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
    void getPassword_success() throws Exception {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginId(loginId);

        MemberQueryResponseDto response = MemberQueryResponseDto.fromEntity(member);

        Mockito.when(queryMemberService.getByLoginId(any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/password-check"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.password", equalTo(member.getPassword())));

        //docs
        result.andDo(document(
                "get-member-password-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지").optional()
                )
        ));
    }
}