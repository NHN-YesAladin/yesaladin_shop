package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeService;

class CommandMemberServiceImplTest {

    private CommandMemberServiceImpl service;
    private CommandMemberRepository commandMemberRepository;
    private QueryMemberRepository queryMemberRepository;
    private QueryMemberGradeService queryMemberGradeService;

    @BeforeEach
    void setUp() {
        commandMemberRepository = Mockito.mock(CommandMemberRepository.class);
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryMemberGradeService = Mockito.mock(QueryMemberGradeService.class);
        service = new CommandMemberServiceImpl(
                commandMemberRepository,
                queryMemberRepository,
                queryMemberGradeService
        );
    }

    @Test
    void create() throws Exception {
        //given
        int id = 1;
        String loginId = "loginId";
        String nickname = "nickname";

        MemberCreateRequest createDto = Mockito.mock(MemberCreateRequest.class);
        MemberGrade memberGrade = Mockito.mock(MemberGrade.class);
        Member member = Member.builder()
                .loginId(loginId)
                .nickname(nickname)
                .memberGrade(memberGrade)
                .build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId)).thenReturn(null);
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname)).thenReturn(null);
        Mockito.when(queryMemberGradeService.findById(id)).thenReturn(memberGrade);

        Mockito.when(createDto.toEntity(memberGrade)).thenReturn(member);

        Mockito.when(commandMemberRepository.save(member)).thenReturn(member);

        //when
        MemberCreateResponse actualMember = service.create(createDto);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getNickname()).isEqualTo(nickname);
        assertThat(actualMember.getMemberGrade()).isEqualTo(memberGrade);
    }
}