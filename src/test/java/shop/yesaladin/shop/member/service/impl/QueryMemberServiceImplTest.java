package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(response.getRole()).hasSize(1);
        assertThat(response.getLoginId()).isEqualTo(loginId);
        assertThat(response.getId()).isEqualTo(memberId);
    }
}