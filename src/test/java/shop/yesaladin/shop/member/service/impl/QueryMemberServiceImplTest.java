package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;

class QueryMemberServiceImplTest {

    private QueryMemberServiceImpl service;
    private QueryMemberRepository repository;
    private QueryMemberRoleRepository queryMemberRoleRepository;

    private Member expectedMember;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(QueryMemberRepository.class);
        queryMemberRoleRepository = Mockito.mock(QueryMemberRoleRepository.class);
        service = new QueryMemberServiceImpl(repository, queryMemberRoleRepository);

        expectedMember = Mockito.mock(Member.class);
    }

    @Test
    void findMemberById_failed_whenMemberNotFound() throws Exception {
        //given
        long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberById(id))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findMemberById() throws Exception {
        //given
        long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getId()).thenReturn(id);

        //when
        MemberDto actualMember = service.findMemberById(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(expectedMember.getId());
    }

    @Test
    void findMemberByNickname_failed_whenMemberNotFound() throws Exception {
        //given
        String nickname = "Ramos";

        Mockito.when(repository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberByNickname(nickname))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findMemberByNickname() throws Exception {
        //given
        String nickname = "Ramos";

        Mockito.when(repository.findMemberByNickname(nickname))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getNickname()).thenReturn(nickname);

        //when
        MemberDto actualMember = service.findMemberByNickname(nickname);

        //then
        assertThat(actualMember.getNickname()).isEqualTo(expectedMember.getNickname());
    }

    @Test
    void findMemberByLoginId_failed_whenMemberNotFound() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(repository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberByLoginId(loginId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findMemberByLoginId() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(repository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getLoginId()).thenReturn(loginId);

        //when
        MemberDto actualMember = service.findMemberByLoginId(loginId);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(expectedMember.getLoginId());
    }

    @Test
    void findMemberLoginInfoByLoginId_failed_whenMemberNotFound() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(repository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> service.findMemberLoginInfoByLoginId(loginId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findMemberLoginInfoByLoginId() throws Exception {
        //given
        String loginId = "test1234";
        Long memberId = 1L;

        Mockito.when(repository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getLoginId()).thenReturn(loginId);
        Mockito.when(expectedMember.getId()).thenReturn(memberId);

        Mockito.when(queryMemberRoleRepository.findMemberRolesByMemberId(memberId))
                .thenReturn(List.of("ROLE_MEMBER"));

        //when
        MemberLoginResponseDto response = service.findMemberLoginInfoByLoginId(
                loginId);

        //then
        assertThat(response.getRoles()).hasSize(1);
        assertThat(response.getLoginId()).isEqualTo(loginId);
        assertThat(response.getId()).isEqualTo(memberId);
    }

    @Test
    void existsMemberByLoginId_whenNotExists_return_false() throws Exception {
        //given
        String loginId = "test1234";

        //when
        boolean result = service.existsLoginId(loginId);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByLoginId_whenExists_return_true() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(repository.existsMemberByLoginId(loginId)).thenReturn(true);

        //when
        boolean result = service.existsLoginId(loginId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByNickname_whenNotExists_return_false() throws Exception {
        //given
        String nickname = "testNickname";

        //when
        boolean result = service.existsLoginId(nickname);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByNickname_whenExists_return_true() throws Exception {
        //given
        String nickname = "testNickname";

        Mockito.when(repository.existsMemberByNickname(nickname)).thenReturn(true);

        //when
        boolean result = service.existsNickname(nickname);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByEmail_whenNotExists_return_false() throws Exception {
        //given
        String email = "test@test.com";

        //when
        boolean result = service.existsLoginId(email);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByEmail_whenExists_return_true() throws Exception {
        //given
        String email = "test@test.com";

        Mockito.when(repository.existsMemberByEmail(email)).thenReturn(true);

        //when
        boolean result = service.existsEmail(email);

        //then
        assertThat(result).isTrue();
    }
}