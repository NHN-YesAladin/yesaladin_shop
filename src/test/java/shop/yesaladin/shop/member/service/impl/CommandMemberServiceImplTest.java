package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberBlockResponse;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.dto.MemberUpdateRequest;
import shop.yesaladin.shop.member.dto.MemberUpdateResponse;
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

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.empty());
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname)).thenReturn(Optional.empty());
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

    @Test
    void update() {
        //given
        long id = 1;
        String name = "name";
        String nickname = "nickname";
        MemberUpdateRequest request = ReflectionUtils.newInstance(MemberUpdateRequest.class, nickname);

        Member member = Member.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname)).thenReturn(Optional.empty());

        //when
        MemberUpdateResponse actualMember = service.update(id, request);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getNickname()).isEqualTo(nickname);
        assertThat(actualMember.getName()).isEqualTo(name);

        verify(queryMemberRepository, times(1)).findById(eq(id));
        verify(queryMemberRepository, times(1)).findMemberByNickname(eq(nickname));
    }

    @Test
    void block() {
        //given
        long id = 1;
        boolean blocked = false;
        String name = "name";
        String loginId = "loginId";

        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .isBlocked(blocked)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));

        //when
        MemberBlockResponse actualMember = service.block(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getName()).isEqualTo(name);
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.isBlocked()).isTrue();

        verify(queryMemberRepository, times(1)).findById(eq(id));
    }

    @Test
    void unblock() {
        //given
        long id = 1;
        boolean blocked = true;
        String name = "name";
        String loginId = "loginId";

        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .isBlocked(blocked)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));

        //when
        MemberBlockResponse actualMember = service.unblock(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getName()).isEqualTo(name);
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.isBlocked()).isFalse();

        verify(queryMemberRepository, times(1)).findById(eq(id));
    }
}