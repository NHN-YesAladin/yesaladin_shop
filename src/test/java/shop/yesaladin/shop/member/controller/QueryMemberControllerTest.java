package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                .andExpect(jsonPath("$.result", equalTo(false)))
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
                .andExpect(jsonPath("$.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsLoginId(loginId);

        //docs
        resultActions.andDo(document(
                "existsLoginId",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("중복 체크 대상 loginId")
                ),
                responseFields(fieldWithPath("result").type(JsonFieldType.BOOLEAN)
                        .description("loginId 중복 여부"))
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
                .andExpect(jsonPath("$.result", equalTo(false)))
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
        ResultActions resultActions = mockMvc.perform(get("/v1/members/checkNick/{nickname}", nickname))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsNickname(nickname);

        //docs
        resultActions.andDo(document(
                "existsNickname",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("nickname").description("중복 체크 대상 nickname")
                ),
                responseFields(fieldWithPath("result").type(JsonFieldType.BOOLEAN)
                        .description("nickname 중복 여부"))
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
                .andExpect(jsonPath("$.result", equalTo(false)))
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
                .andExpect(jsonPath("$.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsEmail(email);

        //docs
        resultActions.andDo(document(
                "existsEmail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("email").description("중복 체크 대상 email")
                ),
                responseFields(fieldWithPath("result").type(JsonFieldType.BOOLEAN)
                        .description("email 중복 여부"))
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
                .andExpect(jsonPath("$.result", equalTo(false)))
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
                .andExpect(jsonPath("$.result", equalTo(true)));

        verify(queryMemberService, times(1)).existsPhone(phone);

        //docs
        resultActions.andDo(document(
                "existsPhone",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("phone").description("중복 체크 대상 phone")
                ),
                responseFields(fieldWithPath("result").type(JsonFieldType.BOOLEAN)
                        .description("phone 중복 여부"))
        ));
    }
}