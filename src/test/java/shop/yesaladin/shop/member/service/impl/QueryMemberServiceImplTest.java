package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberDto;

class QueryMemberServiceImplTest {

    private QueryMemberServiceImpl service;
    private QueryMemberRepository repository;

    private Member expectedMember;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(QueryMemberRepository.class);
        service = new QueryMemberServiceImpl(repository);

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
        System.out.println(actualMember);

        //then
        assertThat(actualMember.getId()).isEqualTo(expectedMember.getId());
    }

    @Test
    void findMemberByNickname() throws Exception {
        //given
//        String nickname = "Ramos";
//
//        Mockito.when(repository.findMemberByNickname(nickname))
//                .thenReturn(Optional.of(expectedMember));
//        Mockito.when(expectedMember.getNickname()).thenReturn(nickname);
//
//        //when
//        MemberDto actualMember = service.findMemberByNickname(nickname);
//
//        //then
//        System.out.println(actualMember);
//        assertThat(actualMember.getNickname()).isEqualTo(expectedMember.getNickname());
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
        System.out.println(actualMember);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(expectedMember.getLoginId());
    }
}