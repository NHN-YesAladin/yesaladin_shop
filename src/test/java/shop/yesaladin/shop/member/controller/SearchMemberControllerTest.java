package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;
import shop.yesaladin.shop.member.service.inter.SearchMemberService;

@WebMvcTest(SearchMemberController.class)
class SearchMemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchMemberService searchMemberService;

    @Autowired
    ObjectMapper objectMapper;

    SearchMemberManagerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = SearchMemberManagerRequestDto.builder()
                .id(0L)
                .loginId("loginId")
                .name("name")
                .nickname("nickname")
                .email("nhn@gmail.com")
                .phone("00000000000")
                .gender(MemberGenderCode.MALE)
                .grade("화이트")
                .birthYear(2017)
                .birthMonth(13)
                .birthDay(23)
                .isWithdrawal(false)
                .isBlocked(false)
                .point(0L)
                .signUpDate(LocalDate.of(2000, 10, 20))
                .build();
    }

    @Test
    @DisplayName("saveMember 메서드의 Validation 에러")
    void saveMemberOfSearchMemberManagerRequestDtoFailByValidationError() throws Exception {
        SearchMemberManagerRequestDto invalidEmailDummy = SearchMemberManagerRequestDto.builder()
                .email("test")
                .build();

        ResultActions resultActions = mockMvc.perform(post("/v1/members/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDummy)));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 등록 성공")
    void saveMemberSuccess() throws Exception {

        SearchedMember searchedMember = requestDto.toSearchedMember();
        Mockito.when(searchMemberService.saveNewMember(any()))
                .thenReturn(searchedMember);

        mockMvc.perform(post("/v1/members/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId", equalTo(requestDto.getLoginId())))
                .andExpect(jsonPath("$.name", equalTo(requestDto.getName())))
                .andExpect(jsonPath("$.nickname", equalTo(requestDto.getNickname())))
                .andExpect(jsonPath("$.email", equalTo(requestDto.getEmail())))
                .andExpect(jsonPath("$.phone", equalTo(requestDto.getPhone())))
                .andExpect(jsonPath("$.grade", equalTo(requestDto.getGrade())))
                .andExpect(jsonPath("$.birthYear", equalTo(requestDto.getBirthYear())))
                .andExpect(jsonPath("$.birthMonth", equalTo(requestDto.getBirthMonth())))
                .andExpect(jsonPath("$.birthDay", equalTo(requestDto.getBirthDay())))
                .andExpect(jsonPath("$.point", equalTo(0)))
                .andExpect(jsonPath("$.signUpDate", equalTo(requestDto.getSignUpDate().toString())))
                .andExpect(jsonPath("$.gender", equalTo(requestDto.getGender().name())))
                .andDo(print());
    }

    @Test
    @DisplayName("updateMember 메서드의 Validation 에러")
    void updateMemberOfSearchMemberManagerRequestDtoFailByValidationError() throws Exception {
        SearchMemberManagerRequestDto invalidEmailDummy = SearchMemberManagerRequestDto.builder()
                .email("test")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/v1/members/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDummy)));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMemberSuccess() throws Exception {

        Mockito.when(searchMemberService.updateMember(any()))
                .thenReturn(requestDto.toSearchedMember());

        mockMvc.perform(put("/v1/members/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId", equalTo(requestDto.getLoginId())))
                .andExpect(jsonPath("$.name", equalTo(requestDto.getName())))
                .andExpect(jsonPath("$.nickname", equalTo(requestDto.getNickname())))
                .andExpect(jsonPath("$.email", equalTo(requestDto.getEmail())))
                .andExpect(jsonPath("$.phone", equalTo(requestDto.getPhone())))
                .andExpect(jsonPath("$.grade", equalTo(requestDto.getGrade())))
                .andExpect(jsonPath("$.birthYear", equalTo(requestDto.getBirthYear())))
                .andExpect(jsonPath("$.birthMonth", equalTo(requestDto.getBirthMonth())))
                .andExpect(jsonPath("$.birthDay", equalTo(requestDto.getBirthDay())))
                .andExpect(jsonPath("$.point", equalTo(0)))
                .andExpect(jsonPath("$.signUpDate", equalTo(requestDto.getSignUpDate().toString())))
                .andExpect(jsonPath("$.withdrawal", equalTo(false)))
                .andExpect(jsonPath("$.blocked", equalTo(false)))
                .andExpect(jsonPath("$.gender", equalTo(requestDto.getGender().name())))
                .andDo(print());
    }

    @Test
    @DisplayName(("회원 정보 삭제 성공"))
    void deleteMemberSuccess() throws Exception {
        mockMvc.perform(delete("/v1/members/search/{loginid}", "loginId"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그인 아이디로 검색")
    void testSearchByLoginId() throws Exception {
        String loginId = "loginId";

        Mockito.when(searchMemberService.searchByLoginId(any()))
                .thenReturn(requestDto.toSearchedMember());

        ResultActions resultActions = mockMvc.perform(get(
                "/v1/members/search/loginid/{loginId}",
                loginId
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId", equalTo(loginId)))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임으로 검색")
    void testSearchByNickname() throws Exception {
        String nickname = "nickname";

        Mockito.when(searchMemberService.searchByNickname(any()))
                .thenReturn(requestDto.toSearchedMember());

        ResultActions resultActions = mockMvc.perform(get(
                "/v1/members/search/nickname/{nickname}",
                nickname
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname", equalTo(nickname)))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임으로 검색")
    void testSearchByPhone() throws Exception {
        String phone = "00000000000";

        Mockito.when(searchMemberService.searchByPhone(any()))
                .thenReturn(requestDto.toSearchedMember());

        ResultActions resultActions = mockMvc.perform(get(
                "/v1/members/search/phone/{phone}",
                phone
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", equalTo(phone)))
                .andDo(print());
    }

    @Test
    @DisplayName("이름으로 검색")
    void testSearchByName() throws Exception {
        String name = "name";
        Mockito.when(searchMemberService.searchByName(name))
                .thenReturn(List.of(requestDto.toSearchedMember()));

        ResultActions resultActions = mockMvc.perform(get(
                "/v1/members/search/name/{name}",
                name
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", equalTo(name)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입날로 검색")
    void testSearchBySignUpDate() throws Exception {
        LocalDate signUpDate = LocalDate.of(2000, 10, 20);
        Mockito.when(searchMemberService.searchBySignUpDate(signUpDate))
                .thenReturn(List.of(requestDto.toSearchedMember()));

        ResultActions resultActions = mockMvc.perform(get(
                "/v1/members/search/signupdate/{signupdate}", signUpDate
        ));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].signUpDate", equalTo(signUpDate.toString())))
                .andDo(print());
    }
}