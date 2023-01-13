package shop.yesaladin.shop.member.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;

class SearchMemberServiceImplTest {

    private SearchMemberRepository searchMemberRepository;
    private SearchMemberServiceImpl searchMemberService;
    private SearchMemberManagerRequestDto dummy;

    @BeforeEach
    void setUp() {
        searchMemberRepository = Mockito.mock(SearchMemberRepository.class);
        searchMemberService = new SearchMemberServiceImpl(searchMemberRepository);
        dummy = Mockito.mock(SearchMemberManagerRequestDto.class);
    }

    @Test
    @DisplayName("로그인 아이디 중복일 경우 익셉션 발생")
    void testSaveLoginIdDuplicationThrMemberProfileAlreadyExistException() {
        String loginId = "loginId";
        Mockito.when(dummy.getLoginId()).thenReturn(loginId);
        Mockito.when(searchMemberRepository.searchByLoginId(loginId))
                .thenReturn(Optional.of(SearchedMember.builder().build()));
        assertThatThrownBy(() -> searchMemberService.saveNewMember(dummy)).isInstanceOf(
                MemberProfileAlreadyExistException.class);
    }

    @Test
    @DisplayName("닉네임 중복일 경우 익셉션 발생")
    void testSaveNicknameDuplicationThrMemberProfileAlreadyExistException() {
        String nickname = "nickname";
        Mockito.when(dummy.getNickname()).thenReturn(nickname);
        Mockito.when(searchMemberRepository.searchByNickname(nickname))
                .thenReturn(Optional.of(SearchedMember.builder().build()));
        assertThatThrownBy(() -> searchMemberService.saveNewMember(dummy)).isInstanceOf(
                MemberProfileAlreadyExistException.class);
    }

    @Test
    @DisplayName("핸드폰 번호 중복일 경우 익셉션 발생")
    void testSavePhoneDuplicationThrMemberProfileAlreadyExistException() {
        String phone = "phone";
        Mockito.when(dummy.getPhone()).thenReturn(phone);
        Mockito.when(searchMemberRepository.searchByPhone(phone))
                .thenReturn(Optional.of(SearchedMember.builder().build()));
        assertThatThrownBy(() -> searchMemberService.saveNewMember(dummy)).isInstanceOf(
                MemberProfileAlreadyExistException.class);
    }

    @Test
    @DisplayName("등록 성공")
    void testSaveSuccess() {
        String loginId = "loginId";
        String nickname = "nickname";
        String phone = "01000000000";

        Mockito.when(dummy.getLoginId()).thenReturn(loginId);
        Mockito.when(dummy.getNickname()).thenReturn(nickname);
        Mockito.when(dummy.getPhone()).thenReturn(phone);

        Mockito.when(searchMemberRepository.searchByLoginId(loginId)).thenReturn(Optional.empty());
        Mockito.when(searchMemberRepository.searchByNickname(nickname))
                .thenReturn(Optional.empty());
        Mockito.when(searchMemberRepository.searchByPhone(phone)).thenReturn(Optional.empty());
        Mockito.when(searchMemberRepository.save(dummy.toSearchedMember())).thenReturn(
                SearchedMember.builder().loginId(loginId).nickname(nickname).phone(phone).build());

        SearchedMember result = searchMemberService.saveNewMember(dummy);

        assertThat(result.getLoginId()).isEqualTo(loginId);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("정보 변경 시 닉네임 중복 시 익셉션")
    void testUpdateMemberDuplicationNicknameThrMemberProfileAlreadyExistException() {
        Long id = 1L;
        Long anotherId = 2L;
        String nickname = "nickname";

        Mockito.when(dummy.getId()).thenReturn(id);
        Mockito.when(dummy.getNickname()).thenReturn(nickname);

        Mockito.when(searchMemberRepository.searchByNickname(nickname)).thenReturn(Optional.of(
                SearchedMember.builder().id(anotherId).nickname(nickname).build()));

        assertThatThrownBy(() -> searchMemberService.updateMember(dummy)).isInstanceOf(
                MemberProfileAlreadyExistException.class);
    }

    @Test
    @DisplayName("정보 변경 시 핸드폰 번호 중복 시 익셉션")
    void testUpdateMemberDuplicationPhoneThrMemberProfileAlreadyExistException() {
        Long id = 1L;
        Long anotherId = 2L;
        String phone = "01000000000";

        Mockito.when(dummy.getId()).thenReturn(id);
        Mockito.when(dummy.getPhone()).thenReturn(phone);

        Mockito.when(searchMemberRepository.searchByPhone(phone)).thenReturn(Optional.of(
                SearchedMember.builder().id(anotherId).phone(phone).build()));

        assertThatThrownBy(() -> searchMemberService.updateMember(dummy)).isInstanceOf(
                MemberProfileAlreadyExistException.class);
    }

    @Test
    @DisplayName("정보 변경 성공")
    void testUpdateSuccess() {
        Long id = 1L;
        String nickname = "nickname";
        String phone = "01000000000";

        Mockito.when(dummy.getNickname()).thenReturn(nickname);
        Mockito.when(dummy.getId()).thenReturn(id);
        Mockito.when(dummy.getPhone()).thenReturn(phone);
        Mockito.when(searchMemberRepository.searchByNickname(nickname))
                .thenReturn(Optional.empty());
        Mockito.when(searchMemberRepository.searchByPhone(phone))
                .thenReturn(Optional.empty());
        Mockito.when(searchMemberRepository.save(dummy.toSearchedMember()))
                .thenReturn(SearchedMember.builder()
                        .id(id)
                        .nickname(nickname)
                        .phone(phone)
                        .build());

        SearchedMember result = searchMemberService.updateMember(dummy);

        assertThat(result.getId()).isEqualTo(dummy.getId());
        assertThat(result.getNickname()).isEqualTo(dummy.getNickname());
        assertThat(result.getPhone()).isEqualTo(dummy.getPhone());
    }

    @Test
    @DisplayName("삭제 성공")
    void testDeleteI() {
        String loginId = "loginId";
        searchMemberService.deleteMember(loginId);
        verify(searchMemberRepository, atLeastOnce()).deleteByLoginId(loginId);
    }

    @Test
    @DisplayName("이름 검색 시 없을 경우 익셉션")
    void testSearchByLoginIdThrMemberNotFoundException() {
        String loginId = "loginId";
        Mockito.when(searchMemberRepository.searchByLoginId(loginId))
                .thenThrow(new MemberNotFoundException(loginId));
        assertThatThrownBy(() -> searchMemberService.searchByLoginId(loginId)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이름 검색 성공")
    void testSearchByLoginIdSuccess() {
        String loginId = "loginId";
        Mockito.when(searchMemberRepository.searchByLoginId(loginId)).thenReturn(Optional.of(
                SearchedMember.builder().loginId(loginId).build()));
        SearchedMember result = searchMemberService.searchByLoginId(loginId);
        assertThat(result.getLoginId()).isEqualTo(loginId);
    }

    @Test
    @DisplayName("이름 검색 시 없을 경우 익셉션")
    void testSearchByPhoneThrMemberNotFoundException() {
        String phone = "phone";
        Mockito.when(searchMemberRepository.searchByLoginId(phone))
                .thenThrow(new MemberNotFoundException(phone));
        assertThatThrownBy(() -> searchMemberService.searchByPhone(phone)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("핸드폰 번호로 검색 성공")
    void testSearchByPhoneIdSuccess() {
        String phone = "phone";
        Mockito.when(searchMemberRepository.searchByPhone(phone)).thenReturn(Optional.of(
                SearchedMember.builder().phone(phone).build()));
        SearchedMember result = searchMemberService.searchByPhone(phone);
        assertThat(result.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("닉네임 검색 시 없을 경우 익셉션")
    void testSearchByNicknameThrMemberNotFoundException() {
        String nickname = "nickname";
        Mockito.when(searchMemberRepository.searchByNickname(nickname))
                .thenThrow(new MemberNotFoundException(nickname));
        assertThatThrownBy(() -> searchMemberService.searchByNickname(nickname)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("닉네임으로 검색 성공")
    void testSearchByNicknameSuccess() {
        String nickname = "nickname";
        Mockito.when(searchMemberRepository.searchByNickname(nickname)).thenReturn(Optional.of(
                SearchedMember.builder().nickname(nickname).build()));
        SearchedMember result = searchMemberService.searchByNickname(nickname);
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("이름으로 검색 시 리스트 출력")
    void testSearchByName() {
        String name = "name";
        List<SearchedMember> results = searchMemberService.searchByName(name);
        assertThat(results).isEmpty();
        verify(searchMemberRepository, atLeastOnce()).searchAllByName(name);
    }

    @Test
    @DisplayName("회원가입날로 검색 시 리스트 출력")
    void testSearchBySignUpDate() {
        LocalDate signUpDate = LocalDate.of(2000, 1,1);
        List<SearchedMember> results = searchMemberService.searchBySignUpDate(signUpDate);
        assertThat(results).isEmpty();
        verify(searchMemberRepository, atLeastOnce()).searchBySignUpDate(signUpDate);
    }
}
