package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
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
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
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

    @Test
    void existsEmail_whenNotExist_return_false() throws Exception {
        //given
        String email = "test@test.com";

        //when
        Mockito.when(queryMemberService.existsEmail(email)).thenReturn(false);

        //then
        mockMvc.perform(get("/v1/members/checkEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.result", equalTo(false)))
                .andDo(print());

        verify(queryMemberService, times(1)).existsEmail(email);
    }

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

    @Test
    void getMemberGrade_fail_memberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getMemberGrade(loginId))
                .thenThrow(new MemberNotFoundException("Member loginId : " + loginId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/grade"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        //docs
        result.andDo(document(
                "get-member-grade-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void getMemberGrade_success() throws Exception {
        //given
        String loginId = "user@1";
        MemberGrade memberGrade = MemberGrade.WHITE;

        MemberGradeQueryResponseDto response = ReflectionUtils.newInstance(
                MemberGradeQueryResponseDto.class, memberGrade.name(), memberGrade.getName());

        Mockito.when(queryMemberService.getMemberGrade(loginId)).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/members/grade"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gradeEn", equalTo(memberGrade.name())))
                .andExpect(jsonPath("$.gradeKo", equalTo(memberGrade.getName())));

        //docs
        result.andDo(document(
                "get-member-grade-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("gradeEn").type(JsonFieldType.STRING)
                                .description("회원 등급 영어 이름"),
                        fieldWithPath("gradeKo").type(JsonFieldType.STRING)
                                .description("회원 등급 한국어 이름")
                )
        ));
    }

    @Test
    void getMemberInfo_fail_memberNotFound() throws Exception {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberService.getByLoginId(loginId))
                .thenThrow(new MemberNotFoundException("Member loginId : " + loginId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/members"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        //docs
        result.andDo(document(
                "get-member-info-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

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
                .andExpect(jsonPath("$.nickname", equalTo(member.getNickname())))
                .andExpect(jsonPath("$.name", equalTo(member.getName())))
                .andExpect(jsonPath("$.loginId", equalTo(member.getLoginId())))
                .andExpect(jsonPath("$.password", equalTo(member.getPassword())))
                .andExpect(jsonPath("$.birthYear", equalTo(member.getBirthYear())))
                .andExpect(jsonPath("$.birthMonth", equalTo(member.getBirthMonth())))
                .andExpect(jsonPath("$.birthDay", equalTo(member.getBirthDay())))
                .andExpect(jsonPath("$.email", equalTo(member.getEmail())))
                .andExpect(jsonPath("$.signUpDate", equalTo(member.getSignUpDate().toString())))
                .andExpect(jsonPath("$.grade", equalTo(member.getMemberGrade().getName())))
                .andExpect(jsonPath(
                        "$.gender",
                        equalTo(member.getMemberGenderCode().getGender() == 1 ? "남" : "여")
                ));

        //docs
        result.andDo(document(
                "get-member-info-success",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("회원의 PK"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("birthYear").type(JsonFieldType.NUMBER)
                                .description("회원의 생년"),
                        fieldWithPath("birthMonth").type(JsonFieldType.NUMBER)
                                .description("회원의 생월"),
                        fieldWithPath("birthDay").type(JsonFieldType.NUMBER)
                                .description("회원의 생일"),
                        fieldWithPath("signUpDate").type(JsonFieldType.STRING)
                                .description("회원의 가입일"),
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("grade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("gender").type(JsonFieldType.STRING)
                                .description("회원의 성별")
                )
        ));
    }
}